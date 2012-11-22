package minimization;

/**
 * <p>Title: Minimization</p>
 * <p>Description: Minimization of deterministic finite state machines</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: BLRC</p>
 * @author Xu, Shun
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class minimization {

  File file = null;
  FSM fsm = new FSM();

  public static void main(String[] args)
  {
      if(args.length == 0)
      {
        System.out.println("There's no input fsm file to minimize.");
        return;
      }
      else
      {
          minimization mini = new minimization(args[0]);
      }

  }

  public minimization(String filename)
  {
          try
          {
            file = new File(filename);
            if(!file.exists())
            {
              System.out.println("Java FSM Parser :  File " + filename + " not existed.");
              return;
            }
          }
          catch (NullPointerException e)
          {
            System.out.println("The filename cannot be null.");
          }
          try
          {
              ReadFSM();
          }
          catch(Exception e)
          {
              System.out.println(e);
          }
          fsm.minimize();
          //generate the output file name
          String name = getFileName(file);
          name += new String("_minimization.fsm");
		  if(file.getParent() != null)
			  name = new String(file.getParent()+name);
          //generating the output file
		  System.out.println("Outputing " + name);
          File fileOutput = null;
          BufferedWriter bufferedwriter = null;
          try
          {
            fileOutput = new File(name);
            fileOutput.createNewFile();
            bufferedwriter = new BufferedWriter(new FileWriter(fileOutput));
            bufferedwriter.write("");
          }
          catch(IOException e)
          {
            System.out.println("Error occured when generating the output file.");
          }
          fsm.output(bufferedwriter);
  }

  private void ReadFSM() throws Exception
  {
	  System.out.println("Reading " + file);
      LineNumberReader linenumberreader = new LineNumberReader(new FileReader(file));
      String s;

      boolean startState = false;
      boolean finalStates = false;

      while((s = linenumberreader.readLine()) != null)
      {
          StringTokenizer stringtokenizer = new StringTokenizer(s);
          if(stringtokenizer.countTokens() == 0)
              continue;
          if(startState == false)
          {
              if(stringtokenizer.countTokens() != 1)
                  throw new Exception("Grammer wrong when defining the start state.");
              fsm.addStartState(s.trim());
              startState = true;
          }
          else if(finalStates == false)
          {
              while(stringtokenizer.hasMoreTokens())
                  fsm.addFinalState(stringtokenizer.nextToken());
              fsm.finalized();
              finalStates = true;
          }
          else
          {
              if(stringtokenizer.countTokens() != 3)
                  throw new Exception("Grammer wrong when defining the transitions.");
              String from = stringtokenizer.nextToken();
              String to = stringtokenizer.nextToken();
              String input = stringtokenizer.nextToken();
              fsm.addTransition(from, to, input);
          }
      }

  }

    private String getFileExtension(File file)
    {
        if(file == null)
            System.out.println("f is null");
        else if(!file.isFile())
        {
            System.out.println("f is not a file");
        }
        else
        {
            String s = file.getName();
            int i = s.lastIndexOf(46);
            if(i > 0 && i < s.length() - 1)
                return s.substring(i + 1).toLowerCase();
        }
        return null;
    }

    private String getFileName(File file)
    {
        if(file == null)
            System.out.println("f is null");
        else if(!file.isFile())
        {
            System.out.println("f is not a file");
        }
        else
        {
            String s = file.getName();
            int i = s.lastIndexOf(46);
            if(i > 0 && i < s.length() - 1)
                return s.substring(0,i).toLowerCase();
        }
        return null;
    }

}