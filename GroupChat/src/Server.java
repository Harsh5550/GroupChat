import java.util.*;
import java.io.IOException;
import java.net.*;
public class Server {
    private ServerSocket ss;
    public Server(ServerSocket ss){
        this.ss=ss;
    }
    public void startServer(){
        try{
            while(!ss.isClosed()){
                Socket s=ss.accept();
                System.out.println("New client has connected....");
                ClientHandler clienthandler= new ClientHandler(s);
                Thread thread=new Thread(clienthandler);
                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void closeServerSocket(){
        try{
            if(ss!=null) {
                ss.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("System IP Address.....:) " + (localhost.getHostAddress()).trim());	
        ServerSocket ss= new ServerSocket(1234);
        Server server= new Server(ss);
        server.startServer();
        server.closeServerSocket();
    }
}
