package gesturedemo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author swang
 */
@WebServlet("/GestureServlet")
public class GestureServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    GestureClient client;

    class GestureClient implements Runnable {

        ObjectInputStream ois;
        boolean updated;
        String gesture;
        Socket socket;

        GestureClient(Socket socket) throws IOException {
            this.socket = socket;
            this.ois = new ObjectInputStream(socket.getInputStream());
            updated = false;
        }

        public void listenForGesture() throws IOException, ClassNotFoundException {
            synchronized (this) {
                if (socket.isInputShutdown()) {
                    System.out.println("Input is shut down");
                }
                gesture = (String) ois.readObject();
                updated = true;
            }
        }

        public String readGesture() {
            updated = false;
            return gesture;
        }

        public String readGesture(boolean block) {
            try {
                listenForGesture();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GestureServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return readGesture();
        }

        @Override
        public void run() {
            try {
                listenForGesture();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GestureServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean connect(int port) {
        Socket socket = null;
        try {
            InetAddress host = InetAddress.getLocalHost();
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //ois = new ObjectInputStream(socket.getInputStream());
            client = new GestureClient(socket);
            //new Thread(client).start();
        } catch (IOException ex) {
            Logger.getLogger(GestureServlet.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public void init() throws ServletException {

        //get the localhost IP address, if server is running on some other IP, you need to use that
        Socket socket = null;
        try {
            InetAddress host = InetAddress.getLocalHost();
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //ois = new ObjectInputStream(socket.getInputStream());
            client = new GestureClient(socket);
            //new Thread(client).start();
        } catch (IOException ex) {
            Logger.getLogger(GestureServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // start client thread that listens to gesture server

    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
        // Besides "text/event-stream;", Chrome also needs charset, otherwise
        // does not work
        // "text/event-stream;charset=UTF-8"
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter out = response.getWriter();

        while (true) {
            //String gesture = client.readGesture();
//            if (client.updated) {
            String gesture = client.readGesture(true);
            if (gesture != null) {
                out.print("id: " + "" + "\n");
                out.print("data: " + gesture + "\n\n");
                out.flush();
            }
//            }

            //Thread.yield();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        //        // Besides "text/event-stream;", Chrome also needs charset, otherwise
        //        // does not work
        //        // "text/event-stream;charset=UTF-8"
        //        response.setContentType("text/event-stream;charset=UTF-8");
        //        response.setHeader("Cache-Control", "no-cache");
        //        response.setHeader("Connection", "keep-alive");
        //
        //        PrintWriter out = response.getWriter();
        //
        //        while (true) {
        //            String gesture = client.readGesture();
        //            out.print("id: " + "ServerTime" + "\n");
        //            out.print("data: " + gesture + "\n\n");
        //            out.flush();
        //        }
    }
}
//}
//}
