package com.spring.utility;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    public String getHome()  throws IOException, JSchException {
        return utility.Exec("pwd").get(0);
    }
    Utility utility = new Utility();

    public List<String> paths(String path) throws IOException, JSchException {
        if(path.length() == 0){
            path = "/";
        }
        // setHome();
        if(!path.contains("/"))
        {
            path+='/';

        } else {
            if(path.charAt(0) == '/')
                path = path.replaceFirst("/","");
            if(path.length()!=0)
                if(path.charAt(path.length()-1)!='/'){
                    path+='/';
                }
        }

        List<String> rslt= new ArrayList<>();
        if(utility.Exec("ls --file-type "+path)!=null)
            for (String p: utility.Exec("ls --file-type "+path)      ) {
                if(p.contains("/")){
                    // rslt.addAll(paths(path+p));
                    paths(path+p).forEach(e->rslt.add(e));
                } else {
                    rslt.add(path+p);
                }
            }
        return rslt;
    }
}
