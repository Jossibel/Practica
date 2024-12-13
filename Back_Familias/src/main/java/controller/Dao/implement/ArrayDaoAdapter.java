package controller.Dao.implement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ArrayDaoAdapter <T>{
  
     private Class<T> type;
    private CustomArrayList<T> customArrayList; 
    private String filePath;
    private Gson gson;

    public ArrayDaoAdapter(Class<T> type) {
        this.type = type;
        this.customArrayList = new CustomArrayList<>();
        this.gson = new Gson();
        this.filePath = "src/main/java/data/" + type.getSimpleName().toLowerCase() + ".json";
    }

    public CustomArrayList<T> list() {
        File file = new File(filePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = TypeToken.getParameterized(CustomArrayList.class, type).getType();
                customArrayList = gson.fromJson(reader, listType);
                if (customArrayList == null) {
                    customArrayList = new CustomArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customArrayList;
    }

    private String transform() {
        return gson.toJson(customArrayList.toArray());
    }

    public void save(T data) {
        list();
        customArrayList.addLast(data);
        writeToFile();
    }

    public void merge(T data, int pos) {
        list();
        try {
            customArrayList.edit(pos, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeToFile();
    }

    public void delete(int pos) {
        list();
        try {
            customArrayList.delete(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeToFile();
    }

    private void writeToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(transform());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
}
