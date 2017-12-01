package GMP;
import GMP.*;
import org.omg.CORBA.SystemException;

public class StartSendMail
{   
    public static void main(String[] args)
    {
    	String host = "84.29.14.11";
    	String user = "";
    	String password = "";
    	String encode = "GB2312";
    	boolean html = true;
    	String from = "";
    	String to = "";
    	String cc = "";
    	String bcc = "";
    	String subject = "";
    	String text = "";
    	String[] accessory = null;
    	    	
    	for ( int i = 0;i < args.length; i ++ )
    	{
    		if ( args[i].equals("-h"))
    		{
    			host = args[++i];
    		}
    		else if ( args[i].equals("-u"))
    		{
    			user = args[++i];
    		}
    		else if ( args[i].equals("-p"))
    		{
    			password = args[++i];
    		}
    		
    		else if ( args[i].equals("-e"))
    		{
    			encode = args[++i];
    		}
    		else if ( args[i].equals("-html"))
    		{
    			html = args[++i].equals("true");
    		}
    		else if ( args[i].equals("-f"))
    		{
    			from = args[++i];
    		}
    		else if ( args[i].equals("-t"))
    		{
    			to = args[++i];
    		}
    		else if ( args[i].equals("-c"))
    		{
    			cc = args[++i];
    		}
    		else if ( args[i].equals("-b"))
    		{
    			bcc = args[++i];
    		}
    		else if ( args[i].equals("-s"))
    		{
    			subject = args[++i];
    		}
    		else if ( args[i].equals("-text"))
    		{
    			text = args[++i];
    		}
    		else if ( args[i].equals("-a"))
    		{
    			accessory = args[++i].split(",");
    			//accessory = new String[]{args[++i]};
    		}
    		else if ( args[i].startsWith("-"))
    		{
				System.out.println("No such parameter");
				System.exit(1);
		    }
		    else
		    {
		    	System.out.println("No such parameter");
				System.exit(1);
		    }
    	}
    	
    	System.out.println();
    	System.out.println("host[�ʼ�������]:"+host);
    	System.out.println("user[�û���]:"+user);
    	System.out.println("password[����]:"+password);
    	System.out.println("encode[���ı���]:"+encode);
    	System.out.println("html[�Ƿ����html]:"+html);
    	System.out.println("from[������]:"+from);
    	System.out.println("to[�ռ���ַ]:"+to);
    	System.out.println("cc[���͵�ַ]:"+cc);
    	System.out.println("bcc[���͵�ַ]:"+bcc);
    	System.out.println("subject[����]:"+subject);
    	System.out.println("text[����]:"+text);
    	System.out.print("accessory[����]:");
    	if ( accessory != null )
    	{
    		for ( int i = 0; i<accessory.length; i ++)
    		{
    			System.out.print(accessory[i]+",");
    		}
    		System.out.println();
    	}
    	else
    	{
    		System.out.println("null");
    	}
    			  
    	System.out.println();
    	System.out.println("Start send Mail ......");
    	
        GMP.sendmail mail = new GMP.sendmail( encode,html ); 
        try
        {
        	System.out.println("Connecting ......");
        	mail.connect( host,"","" );
        	System.out.println("Connected ......");
        }
        catch(Exception ex)
        {
        	System.out.println("Failed to connect ......");
        	mail.close();
        	System.exit(1);
        }
                  
        try
        {
        	System.out.println("Sending ......");
        	mail.send( from,to,cc,bcc,subject,text,accessory);
        	System.out.println("Sended ......");
        }            
        catch(Exception ex)
        {
        	System.out.println("Failed to send ......"+ex);
        	mail.close();
        	System.exit(1);
        }
        finally
        {
        	mail.close();
        }   	
    }
}
