package com.track.until;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfUtil {

    public static String readConfigFile(String cfgFile) {
        String readStr = null;
        try {
            // 这里就以读取文件流为例,这种就会读取resources下的conf.yml文件(不能在前面加/)
            InputStream in = YamlConf.class.getClassLoader().getResourceAsStream("application.yml");
            Properties prop = new Properties();
            prop.load(new InputStreamReader(in, "UTF-8"));
            readStr = prop.getProperty(cfgFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return readStr;
    }

}
