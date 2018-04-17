package Main;

import model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private ConcurrentHashMap<String, ConsumoMensal> hashConsumoAtual = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConsumoMensal> hashConsumoAnterior = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Fatura> hashFatura = new ConcurrentHashMap<>();

    private static Controller controller;

    private Controller (){}

    public static Controller getInstance(){
        if (controller == null){
            controller = new Controller();
        }
        return controller;
    }

    public void getSensorData(Message message){
        ArrayList<Consumo> listaConsumo;
        ConsumoMensal consumoMensal;
        Fatura f;

        Residencia r = (Residencia)message.getObject();

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

        if (hashFatura.containsKey(r.getId())){
            hashFatura.get(r.getId()).setConsumoTotal(consumoMensal.getConsumoTotal());
            f = hashFatura.get(r.getId());
        } else {
            hashFatura.put(r.getId(), new Fatura(r.getId(), consumoMensal.getConsumoTotal()));
        }
    }

    public boolean checkID(Message message){
        String id = (String)message.getObject();
        return (hashConsumoAnterior.containsKey(id) || hashConsumoAtual.containsKey(id));
    }

    public ConsumoMensal getConsumoAtual (Message message){
        String id = (String)message.getObject();
        return hashConsumoAtual.get(id);
    }

    public ConsumoMensal getConsumoAnterior (Message message){
        String id = (String)message.getObject();
        return hashConsumoAnterior.get(id);
    }

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
    }

    public void getZonaStatus(Message message){

    }

/*    public void setMeta (Message message){
        Fatura f = (Fatura)message.getObject();

        if (hashFatura.containsKey(f.getId())){
            hashFatura.get(f.getId()).setMeta(f.getMeta(), f.getEmail());
        } else {
            hashFatura.put(f.getId(), new Fatura(f.getMeta(), f.getEmail(), f.getId()));
        }
    }*/

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
