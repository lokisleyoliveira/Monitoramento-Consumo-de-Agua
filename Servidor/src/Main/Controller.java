package Main;

import model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private ConcurrentHashMap<String, ConsumoMensal> hashConsumoAtual = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConsumoMensal> hashConsumoAnterior =  null;
    private ConcurrentHashMap<String, Fatura> hashFatura = new ConcurrentHashMap<>();
    private ArrayList<Zona> listZona = new ArrayList<>();

    private static Controller controller;

    private Controller (){}

    public static Controller getInstance(){
        if (controller == null){
            controller = new Controller();
        }
        return controller;
    }

    /**
     * Realiza a persistencia dos dados em um aruqivos
     * @throws IOException
     */
    public void salvaArquivo() throws IOException {
        File file = new File("Backup.ag");
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        out.writeObject(hashConsumoAtual);
        out.flush();
        out.writeObject(hashConsumoAnterior);
        out.flush();
        out.writeObject(listZona);
        out.flush();
        out.close();
    }

    /**
     * Le os objetos apartir do arquivo de persistencia
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void leArquivo() throws IOException, ClassNotFoundException {
        File file = new File("Bacukp.ag");
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        hashConsumoAtual = (ConcurrentHashMap<String, ConsumoMensal>) in.readObject();
        hashConsumoAnterior = (ConcurrentHashMap<String, ConsumoMensal>) in.readObject();
        hashFatura = (ConcurrentHashMap<String, Fatura>) in.readObject();

        if(hashConsumoAtual == null){
            hashConsumoAtual = new ConcurrentHashMap<>();
        }
        if(hashConsumoAnterior == null){
            hashConsumoAnterior = new ConcurrentHashMap<>();
        }
        if(hashFatura == null){
            hashFatura = new ConcurrentHashMap<>();
        }
        in.close();
    }

    /**
     * Armazena os dados recebidos do sensor
     * @param message
     */
    public void getSensorData(Message message){
        ArrayList<Consumo> listaConsumo;
        ConsumoMensal consumoMensal;
        Fatura f;
        Iterator i = listZona.iterator();
        Zona z = null;

        Residencia r = (Residencia)message.getObject();



        System.out.println("Dados recebidos:" + r.getId() + " " +
                        r.getVazao() + " " + r.isAgua() + " " +
                        r.getZona() + " " + r.getDate());

        //salva os dados do sensor na hash, e calcula o valor de consumo, no comsumo total do mes
        if (hashConsumoAtual.containsKey(r.getId())){
            consumoMensal = hashConsumoAtual.get(r.getId());
            listaConsumo = consumoMensal.getList();
            listaConsumo.add(new Consumo (r.getVazao()*5, new Date()));
            consumoMensal.setConsumoTotal(consumoMensal.getConsumoTotal() + r.getVazao()*5);
            consumoMensal.setValorEstimado(valorFatura(consumoMensal.getConsumoTotal()));
        } else {
            listaConsumo = new ArrayList<>();
            listaConsumo.add(new Consumo(r.getVazao(), r.getDate()));
            consumoMensal = new ConsumoMensal(listaConsumo, r.getVazao()*5, 27.5);
            hashConsumoAtual.put(r.getId(), consumoMensal);
        }

        //adciona o total de consumo no objeto que gerará a fatura
        if (hashFatura.containsKey(r.getId())){
            hashFatura.get(r.getId()).setConsumoTotal(consumoMensal.getConsumoTotal());
            f = hashFatura.get(r.getId());
        } else {
            hashFatura.put(r.getId(), new Fatura(r.getId(), consumoMensal.getConsumoTotal()));
        }

        //relaciona os dados do sensor à zona ao qual ele está relacionado
        while (i.hasNext()){
            z = (Zona)i.next();
            if (z.getZona() == r.getZona()){
                z.addUpdate(r);
            }
        }
        if(z == null){
            listZona.add(new Zona(r));
        }
    }

    /**
     * verifica se um ID está presente no servidor
     */
    public boolean checkID(Message message){
        String id = (String)message.getObject();
        return (hashConsumoAtual.containsKey(id) || hashConsumoAnterior.containsKey(id));
    }

    /**
     * Retorna a relaçao de consumo do mes corrente do ID solicitado
     * @param message
     * @return
     */
    public ConsumoMensal getConsumoAtual (Message message){
        String id = (String)message.getObject();
        return hashConsumoAtual.get(id);
    }

    /**
     * Retorna a relação de consumo do mes anterior do ID solicitado
     * @param message
     * @return
     */
    public ConsumoMensal getConsumoAnterior (Message message){
        String id = (String)message.getObject();
        return hashConsumoAnterior.get(id);
    }

    /**
     * Percorre os dados de consumos de cada Consumidor, para gerar o valor da fatura
     * @param message
     */
    public void geraFaturas (Message message){
        Set<Map.Entry<String, Fatura>> entrySet = hashFatura.entrySet();
        Iterator<Map.Entry<String, Fatura >> i = entrySet.iterator();

        while (i.hasNext()){
            Map.Entry<String, Fatura> entry = i.next();
            String id = entry.getKey();

            Fatura f = hashFatura.get(id);

            hashFatura.get(id).setValor(valorFatura(f.getConsumoTotal()));

            System.out.println(
                    "Fatura de " + id +
                    "- Consumo total: " + f.getConsumoTotal() +
                    ", Valor da Fatura: R$ " + f.getValor());
        }

        hashConsumoAnterior = hashConsumoAtual;
        hashConsumoAtual = new ConcurrentHashMap<>();
    }

    /**
     * Retorna o status atual de todas as zonas, se há vazamento/escassez ou nao
     * @param message
     * @return
     */
    public String getZonaStatus(Message message){
        String s = ("Status das zonas\n");
        Iterator i = listZona.iterator();
        Zona z;
        while (i.hasNext()) {
            z = (Zona)i.next();
            s = s.concat("Zona " + z.getZona());
            if (z.isEscassez()){
                s = s.concat(" ESCASSEZ");
            }
            if (z.isVazamento()){
                s = s.concat(" VAZAMENTO");
            }
            if (!z.isVazamento() && !z.isEscassez()){
                s = s.concat("OK");
            }
            s = s.concat("\n");
        }
        return s;
    }

/*    public void setMeta (Message message){
        Fatura f = (Fatura)message.getObject();

        if (hashFatura.containsKey(f.getId())){
            hashFatura.get(f.getId()).setMeta(f.getMeta(), f.getEmail());
        } else {
            hashFatura.put(f.getId(), new Fatura(f.getMeta(), f.getEmail(), f.getId()));
        }
    }*/

    /**
     * Calcula o valor da fatura baseado na quantidade consumida
     * @param consumo
     * @return
     */
    private Double valorFatura(Double consumo){
        if (consumo <= 6){
            return 27.5;
        } else if (consumo > 6 && consumo <= 10){
            return (27.5 + (consumo-6)*1.09);
        } else if (consumo > 10 && consumo <= 15){
            return (31.86 + (consumo-10)*7.68);
        } else if (consumo > 15 && consumo <= 20){
            return (44.72 + (consumo-15)*8.22);
        } else if (consumo > 20 && consumo <= 25){
            return (85.82 + (consumo-20)*9.24);
        } else if (consumo > 25 && consumo <= 30){
            return (132.02 + (consumo-25)*10.31);
        } else if (consumo > 30 && consumo <= 40){
            return (183.57 + (consumo-30)*11.34);
        } else if (consumo > 40 && consumo <= 50){
            return (296.97 + (consumo-40)*12.43);
        } else {
            return (421.27 + (consumo-50)*14.95);
        }
    }
}
