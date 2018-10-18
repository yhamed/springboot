package com.spring.utility;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Utility {
    public final static List<String> Exec(String cmd) throws JSchException, IOException {

        List<String> msgs = new ArrayList<>();

        JSch jsch = new JSch();
        Session session = jsch.getSession("hamedy", "ssh-domain", 22);
        session.setPassword("yourpassword");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();


        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));

        channel.setCommand(cmd);
        channel.connect();

        String msg = null;
        while ((msg = in.readLine()) != null) {
            msgs.add(msg);
        }

        channel.disconnect();
        session.disconnect();

        return msgs;
    }
    public InputStream getFile(String path) throws JSchException, IOException, SftpException {

        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession("hamedy", "ssh-domain", 22);
            session.setPassword("yourpassword");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            System.out.println("Starting File download");
            channelSftp.get(path,"C:\\Users\\hyounes\\Desktop\\test.pdf");
            InputStream is = channelSftp.get(path);
                    //     IOUtils.copy(is, System.out);
            is.close();


            channelSftp.exit();
            session.disconnect();
            System.out.println("done");
            return is;
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return null;
    }
}
