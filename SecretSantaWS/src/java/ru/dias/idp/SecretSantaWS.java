/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dias.idp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;


/**

 *
 * @author vkovrizhnikov
 */
@WebService(serviceName = "SecretSantaWS")
public class SecretSantaWS {
    
    /**
     * Операция веб-службы
     * @param mail
     * @param subject
     * @param message
     */
    @WebMethod(operationName = "SendLetters")
    public String SendLetters(@WebParam(name = "mail") String mail,
                              @WebParam(name = "subject") String subject,
                              @WebParam(name = "login") String login,
                              @WebParam(name = "password") String password,
                              @WebParam(name = "from") String from,
                              @WebParam(name = "host") String host,
                              @WebParam(name = "port") String port,
                              @WebParam(name = "message") String message) throws IOException {
        //TODO write your implementation code here:
        EMailHandler mailHandler = null;
        String state = "OK - Все сообщения отправлены успешно.";
        
        try{
        mailHandler = new EMailHandler(host,port,login,password);
        sendEmail(mailHandler, mail, subject, message, from);
        } catch (Exception e) {
                    // Oops...
                    message = "Вернулась ошибка " + e;
                    e.printStackTrace();
                    state = "FAIL - Видимо в другой раз.";
                    
                }
        
        return state;
    }
    
    private void sendEmail(EMailHandler mailHandler, String mailList, String subject, String body, String stfrom) {
        // Split list by ";"
        String[] recipientList = mailList.split(";");
        String[] assignedList = Arrays.copyOf(recipientList, recipientList.length);
        
        Collections.shuffle(Arrays.asList(assignedList));
        // From?
        String from = stfrom;
        // Send notification for recipients step by step
        for (String to : recipientList) {
            if (to != null && !to.isEmpty()) {
                for (int i = 0; i < assignedList.length; i++){
                    if (assignedList[i] != null && !assignedList[i].equals(to)){
                        try {
                            if (null == from || "".equals(from.trim())){
                                mailHandler.send(to, to, subject, body + assignedList[i], "utf-8");
                            } else {
                                mailHandler.send(from, to, subject, body + assignedList[i], "utf-8");
                            }
                        } catch (Exception e) {
                            // Oops...   
                        }
                        assignedList[i] = null;
                        break;
                    }
                
                }
                
                
            }
        }
    }
}
