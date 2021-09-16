package com.company;

import java.io.*;
import java.util.zip.*;

public class Main {
    final static String ROOT_DIR = "C://games//savegames";
    final static String FILE_END = ".dat";
    static StringBuilder LOG;
    // распаковка
    static void unZip(String pathFrom, String pathTo) {
        File fromZip = new File(pathFrom);
        File toDir = new File(pathTo);
        String name;
        // переданы корректные параметры
        if(fromZip.isFile() && toDir.isDirectory()) {
            // распаковка
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fromZip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    name = entry.getName();
                    // поток - получатель
                    FileOutputStream fos = new FileOutputStream(toDir.getPath() + "//" + name);
                    for (int c = zis.read(); c != -1; c = zis.read()) {
                        fos.write(c);
                    }
                    fos.flush();
                    fos.close();
                    zis.closeEntry();
                }
            } catch (IOException exp) {
                LOG.append("\nОшибка при открытии архива: ").append(exp.getMessage());
            }
        }
    }
    // открыть сохранение
    static GameProgress openProgress(String path) {
        GameProgress saveRec = null;
        File save = new File(path);
        // передан файл
        if (save.isFile()) {
            // получение данных
            try (FileInputStream fis = new FileInputStream(save); ObjectInputStream oos = new ObjectInputStream(fis)) {
                saveRec = (GameProgress) oos.readObject();
            } catch (Exception exp) {
                LOG.append("\nОшибка при десериализации файла ").append(save.getName()).append(" :").append(exp.getMessage());
            }
        }
        return saveRec;
    }

    public static void main(String[] args) {
        File wrkDir = new File(ROOT_DIR);
        LOG = new StringBuilder();
        // распаковка архива
        unZip(wrkDir + "//saves.zip", ROOT_DIR);
        GameProgress saveRec;
        // смотрим все сохранения из архива
        for(File save : wrkDir.listFiles((a,b) -> b.endsWith(FILE_END))){
            // получаем данные
            saveRec = openProgress(save.getPath());
            // вывод в консоль
            System.out.println(saveRec.toString());
        }
        if(!LOG.isEmpty()) System.out.println(LOG.toString());
    }
}
