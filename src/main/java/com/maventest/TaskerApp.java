package com.maventest;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class TaskerApp {
    int completed = 0;
    ArrayList<String> todoList = new ArrayList<>();
    int exit = 0;
    int id = 0;
    int unsaved = 0;
    int maxId=0;

    public void start() {
        // String input;
        unsaved = 0;
        maxId=0;
        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
        String additInfo ="";
        System.out.println("Добро пожаловать в \"Таск Манагер by Den_Alf\"");


        try {
            while(exit==0) {
                System.out.print("Введите команду: ");
                String input = inp.readLine();
                int spaceIndex = input.indexOf(" ");
                if(spaceIndex!=-1){
                    additInfo = input.substring(spaceIndex+1);
                    input = input.substring(0,spaceIndex);
                }
                String[] substr=input.split(" ");
                String command = substr[0];


                switch (command){
                    case "help": help(); // Инфа
                        break;

                    case "exit": exit=1; // Выход
                        break;

                    case "add": add(additInfo);//Добавление задач
                        break;

                    case "all": all();  //Вывод списка задач
                        break;

                    case "delete": delete(additInfo); //Удаление задач
                        break;

                    case "save": save(additInfo);  //Сохранение в файл
                        break;

                    case "load":  load(additInfo); //Загрузка из файла
                        break;

                    case "complete": complete(additInfo);  //Отметка о выполнении
                        break;

                    case "completed": completed();  //Вывод выполненных задач
                        break;

                    default: String line = "Командна неопознана, попробуйте ещё раз" + System.lineSeparator() + "Для вызова подсказки используйте help";
                        System.out.println(line);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void completed(){
        int i = 0;
        Collections.sort(todoList);
        if (completed > 0) {
            while (i < completed) {
                System.out.println(todoList.get(i));
                i+=1;
            }
        }
        else System.out.println("Вы ещё не выполнили ни одной задачи");
    }

    public void complete(String completedId){
        int founder = -1;
        int i = 0;
        int completer = 0;
        while((founder == -1)&i<id ){
            String presentStr = todoList.get(i);
            founder = presentStr.indexOf("(id:"+completedId+")");
            completer=i;
            i+=1;
        }
        if (founder==-1) System.out.println("Строка с данным id не найдена");
        else{
            String piece = todoList.get(completer).substring(0,todoList.get(completer).indexOf("(id:"));
            if(piece.contains("(Completed)")){
                System.out.println("Задача уже была выполнена ранее");
            }
            else {
                todoList.set(completer, "(Completed) " + todoList.get(i - 1));
                System.out.println("Задача выполнена");
                completed+=1;
                unsaved+=1;
                Collections.sort(todoList);
            }
        }
    }

    public void save(String fileName) {
        int ok = 1;
        try {
            File file = new File(fileName);
            if (file.length() != 0) {
                int answer = 0;
                BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
                while (answer==0) {
                    System.out.print("Файл для записи не пустой, хотите перезаписать файл? Y/N: ");
                    String input = inp.readLine();
                    switch (input) {
                        case "Y":
                            ok = 1;
                            answer = 1;
                            break;

                        case "N":
                            ok = -1;
                            System.out.println("Отмена сохранения в файл");
                            answer = 1;
                            break;

                        default:
                            System.out.println("Команда не распознана, ответьте 'Y' если хотите перезаписать файл или 'N' для отмены сохранения ");
                    }
                }
            }
            if (ok == 1) {
                FileWriter writer = new FileWriter(fileName, false);
                for (int i = 0; i < todoList.size(); i++) {
                    String text = todoList.get(i) + System.lineSeparator();
                    writer.write(text);
                }
                writer.flush();
                unsaved=0;
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void delete(String deletedId){
        int founder = -1;
        int i = 0;
        int deliter = 0;
        while((founder == -1)&i<id ){
            String presentStr = todoList.get(i);
            founder = presentStr.indexOf("(id:"+deletedId+")");
            deliter=i;
            i+=1;
        }
        if (founder==-1) System.out.println("Строка с данным id не найдена");
        else  {
            if (todoList.get(deliter).length()>12) {
                if (todoList.get(deliter).substring(0, 11).contains("(Completed)")) {
                    completed -= 1;
                }
            }
            todoList.remove(deliter);
            unsaved+=1;
        }
    }

    public void all(){
        if(todoList.isEmpty()) System.out.println("Cписок задач - пуст");
        else{
            Collections.sort(todoList);
            for (int i = 0; i < todoList.size(); i++) {
                System.out.println(todoList.get(i));
            }
        }
    }

    public void add(String taskText){
       /* if(!todoList.isEmpty()) {

            if(completed!=0){
                String lastCompl = todoList.get(completed-1);
                int completedOut = lastCompl.indexOf(" ");
                String piece = lastCompl.substring(completedOut);
                int idEnd = piece.indexOf(") ");
                int idStart = piece.indexOf(":") + 1;
                lastCompl = piece.substring(idStart,idEnd);
                id = Integer.parseInt(lastCompl.trim());
            }
            if(completed<todoList.size()) {
                Collections.sort(todoList);
                String lastStr = todoList.get(todoList.size() - 1);
                int idStart = lastStr.indexOf(":") + 1;
                int idEnd = lastStr.indexOf(") ");
                lastStr = lastStr.substring(idStart, idEnd);
                if (id < Integer.parseInt(lastStr.trim())) {
                    id = Integer.parseInt(lastStr.trim());
                }
            }

            //id = todoList.size();
        }*/

        String task1 = "(id:" + (maxId + 1) + ") " + taskText;
        todoList.add(task1);
        unsaved+=1;
        maxId+=1;
    }

    public  void load(String fileName){
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufReader = new BufferedReader(reader);
            int ok =1;
            if (unsaved != 0) {
                int answer =0;
                BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
                while (answer==0) {
                    System.out.print("Вы не сохранили " + unsaved + " последних изменений, хотите продолжить загрузку?  Y/N: ");
                    String input = inp.readLine();
                    switch (input) {
                        case "Y":
                            ok = 1;
                            answer=1;
                            break;

                        case "N":
                            ok = -1;
                            System.out.println("Отмена загрузки в файл");
                            answer=1;
                            break;

                        default:
                            System.out.println("Команда не распознана, ответьте 'Y' если хотите загрузить файл без сохранения изменений или 'N' для отмены загрузки ");

                    }
                }
            }
            if(ok==1) {
                String line = bufReader.readLine();
                completed = 0;
                todoList.clear();
                while (line != null) {
                    int completedLine = 0;
                    if (line.contains("(id:")) {
                        if (line.substring(0, line.indexOf("(id:")).contains("(Completed)")) {
                            completed += 1;
                            int completedOut = line.indexOf(" ");
                            String piece = line.substring(completedOut);
                            int idEnd = piece.indexOf(") ");
                            int idStart = piece.indexOf(":") + 1;
                            String lastCompl = piece.substring(idStart,idEnd);
                            if(maxId< Integer.parseInt(lastCompl.trim())){
                                maxId=Integer.parseInt(lastCompl.trim());
                            }
                            completedLine=1;
                        }
                    }
                    else {
                        System.out.println("Неверный формат записей (id задач не указано или указано неверно)");
                        break;
                    }

                    todoList.add(line);
                    if(completedLine==0) {
                        int idStart = line.indexOf(":") + 1;
                        int idEnd = line.indexOf(") ");
                        String lastStr = line.substring(idStart, idEnd);
                        if (maxId < Integer.parseInt(lastStr.trim())) {
                            maxId = Integer.parseInt(lastStr.trim());
                        }
                    }
                    line = bufReader.readLine();
                }
                unsaved = 0;
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void help() {
        String helpInfo = "add [Текст задачи] - создает новую задачу" + System.lineSeparator()+
                "all - выводит все задачи" + System.lineSeparator() +
                "delete [id] - удаляет задачу по идентификатору (который должен отображаться в all)" + System.lineSeparator() +
                "save [file-name.txt] - сохраняет все текущие задачи в указанный файл" + System.lineSeparator() +
                "load [file-name.txt] - загружает задачи с файла" + System.lineSeparator() +
                "complete [id] - выставляет, что задача выполнена" + System.lineSeparator() +
                "completed - выводит все выполненные задачи" + System.lineSeparator() +
                "exit - завершение работы программы";
        System.out.println(helpInfo);
    }
}