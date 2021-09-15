package com.company;

import java.io.*;
import java.util.zip.*;

public class Main {
    final static String ROOT_DIR = "C://games//savegames";
    final static String FILE_END = ".dat";

    static void unZip(String pathFrom, String pathTo) {
        File fromDir = new File(pathFrom);
        File toDir = new File(pathTo);
        String name;
        // переданы директории
        if(fromDir.isDirectory() && toDir.isDirectory()) {
            // смотрим все архивы в источнике
            for (File arch : fromDir.listFiles((a, b) -> b.endsWith(".zip"))) {
                // распаковка
                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arch))) {
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
                    System.out.println("Ошибка при открытии архива: " + exp.getMessage());
                }
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
                //System.out.println(saveRec.toString());
            } catch (Exception exp) {
                System.out.println("Ошибка при десериализации файла " + save.getName() + " :" + exp.getMessage());
            }
        }
        return saveRec;
    }

    public static void main(String[] args) {
        File wrkDir = new File(ROOT_DIR);
        // распаковка архива
        unZip(ROOT_DIR, ROOT_DIR);
        GameProgress saveRec;
        // смотрим все сохранения из архива
        for(File save : wrkDir.listFiles((a,b) -> b.endsWith(FILE_END))){
            // получаем данные
            saveRec = openProgress(save.getPath());
            // вывод в консоль
            System.out.println(saveRec.toString());
        }
    }
}
