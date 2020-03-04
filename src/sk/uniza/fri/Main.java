package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        // join a Multicast group and send the group salutations
        try {
            final InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket s = new MulticastSocket(6789);
            s.joinGroup(group);

            new Thread(() -> {
                BufferedReader inputBfReader = new BufferedReader(new InputStreamReader(System.in));
                String inputString;
                try {
                    while ((inputString = inputBfReader.readLine()) != null) {
                        DatagramPacket hi = new DatagramPacket(inputString.getBytes(StandardCharsets.UTF_8), inputString.length(),
                                group, 6789);
                        s.send(hi);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }).start();
            if(args.length==0) {
                // get their responses!
                byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                while (true) {
                    s.receive(recv);
                    System.out.println("From Addr:" + recv.getAddress() + ": " + new String(recv.getData(), StandardCharsets.UTF_8));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

