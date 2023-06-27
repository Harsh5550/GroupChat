import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{
    private Socket s;
    private BufferedWriter bw;
    private BufferedReader br;
    private String username;
    public Client(Socket s, String username){
        try{
            this.s=s;
            this.bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.br= new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.username=username;
        }
        catch (IOException e){
            closeEverything(s, br, bw);
        }

    }
    public void sendMessage(){
        try{
            bw.write(username);
            bw.newLine();
            bw.flush();
            Scanner sc=new Scanner(System.in);
            while(s.isConnected()){
                String message=sc.nextLine();
                bw.write(username+": "+message);
                bw.newLine();
                bw.flush();
            }
        }
        catch (IOException e){
            closeEverything(s, br, bw);
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgfromChat;
                while(s.isConnected()){
                    try{
                        msgfromChat=br.readLine();
                        System.out.println(msgfromChat);
                    }
                    catch (IOException e){
                        closeEverything(s, br, bw);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket s, BufferedReader br, BufferedWriter bw){
        try{
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
            if(s!=null){
                s.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter your IP Address displayed on Server...");
        String ip=sc.nextLine();
        System.out.println("Enter your GroupChat username...");
        String username=sc.nextLine();
        Socket s=new Socket( ip, 1234);
        Client client=new Client(s, username);
        client.listenForMessage();
        client.sendMessage();

    }
}
