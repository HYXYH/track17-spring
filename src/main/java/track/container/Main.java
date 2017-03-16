package track.container;

import track.container.beans.Car;
import track.container.beans.Gear;
import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

        List<Bean> beans;
        ConfigReader reader = new JsonConfigReader();
        try {
            beans = reader.parseBeans(new File("config.json"));

            Container container = new Container(beans);
            Car car = null;
            Gear gear = null;
            gear = (Gear) container.getById("gearBean");
            System.out.println(gear.toString());
            car = (Car) container.getByClass("track.container.beans.Car");
            System.out.println(car.toString());
            System.out.println(gear.equals(car.getGear()));
        } catch (GetBeanException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
