package Main;

import model.Consumo;
import model.Message;
import model.Residencia;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private ConcurrentHashMap<String, ArrayList<Consumo>> hashConsumo = new ConcurrentHashMap<String, ArrayList<Consumo>>();

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

        Residencia r = (Residencia)message.getObject();
        if (hashConsumo.containsKey(r.getId())){
            listaConsumo = hashConsumo.get(r.getId());
            listaConsumo.add(new Consumo(r.getVazao(), r.getDate()));
        } else {
            listaConsumo = new ArrayList<>();
            listaConsumo.add(new Consumo(r.getVazao(), r.getDate()));
            hashConsumo.put(r.getId(), listaConsumo);
        }
    }
}
