import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList <ClientHandler> clientHandlers=new ArrayList<>();
    private Socket s;
    private BufferedReader br;
    private BufferedWriter bw;
    private String clientUsername;
    public ClientHandler(Socket s){
        try{
            this.s=s;
            this.bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.clientUsername=br.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: "+clientUsername+" has entered chatroom...");
        }
        catch (IOException e){
            closeEverything(s, br, bw);
        }
    }
    @Override
    public void run() {
        String messageFromClient;
        while(s.isConnected()){
            try{
                messageFromClient=br.readLine();
                broadcastMessage(messageFromClient);
            }
            catch (IOException e){
                closeEverything(s, br, bw);
                break;
            }
        }

    }
    public void broadcastMessage(String message){
        for(ClientHandler clientHandler:clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bw.write(message);
                    clientHandler.bw.newLine();
                    clientHandler.bw.flush();
                }
            }
            catch (IOException e){
                closeEverything(s, br, bw);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+clientUsername+" has left...");
    }
    public void closeEverything(Socket s, BufferedReader br, BufferedWriter bw){
        removeClientHandler();
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
}
