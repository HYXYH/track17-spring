package track.container;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;
import track.container.config.Root;


public class JsonConfigReader implements ConfigReader {

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {
        Root root;
        ObjectMapper mapper = new ObjectMapper();

        try {
            root = mapper.readValue(configFile, Root.class);
        } catch (IOException e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
        return root.getBeans();
    }

}
