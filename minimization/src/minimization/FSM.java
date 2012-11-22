package minimization;

/**
 * <p>Title: Minimization</p>
 * <p>Description: Minimization of deterministic finite state machines</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: BLRC</p>
 * @author Xu, Shun
 * @version 1.0
 */

import java.util.*;
import java.io.*;

public class FSM {

  int startIndex;  /*index of the start state*/
  String startStateName;
  ArrayList states = new ArrayList(); /*all the states*/
  Hashtable finalStates = new Hashtable(); /*final states*/
  Hashtable nonFinalStates = new Hashtable();  /*non-final states*/
  Hashtable inputIndex = new Hashtable();
  ArrayList inputs = new ArrayList(); /*input sets*/
  Int2DArray transitions = new Int2DArray();

  boolean[][] Distinguished;
  ArrayList[][] Pending;

  ArrayList toDelete = new ArrayList();
  HashSet deleteSet = new HashSet();

  public FSM()
  {

  }

  public void addStartState(String name)
  {
      startStateName = new String(name);
      states.add(startStateName);
      startIndex = states.size() - 1;
  }

  public void addFinalState(String name) throws Exception
  {
      states.add(name);
      int index = states.size() - 1;
      try
      {
          finalStates.put(name,new Integer(index));
      }
      catch(NullPointerException e)
      {
          throw new Exception("Error happened when reading the definition of final states.");
      }

  }

  public void finalized()
  {
      if(!finalStates.contains(startStateName))
          nonFinalStates.put(startStateName,new Integer(startIndex));
  }

  public void addTransition(String fromNode, String toNode, String input)
  {
      Integer indexFrom;
      Integer indexTo;
      Integer indexInput;
      if(states.contains(fromNode))
      {
          indexFrom = (Integer)finalStates.get(fromNode);
          if(indexFrom == null)
              indexFrom = (Integer)nonFinalStates.get(fromNode);
      }
      else
      {
          states.add(fromNode);
          indexFrom = new Integer(states.size() - 1);
          nonFinalStates.put(fromNode, indexFrom);
      }
      if(states.contains(toNode))
      {
          indexTo = (Integer)finalStates.get(toNode);
          if(indexTo == null)
              indexTo = (Integer)nonFinalStates.get(toNode);
      }
      else
      {
          states.add(toNode);
          indexTo = new Integer(states.size() - 1);
          nonFinalStates.put(toNode, indexTo);
      }
      if(inputs.contains(input))
      {
          indexInput = (Integer)inputIndex.get(input);
      }
      else
      {
          inputs.add(input);
          indexInput = new Integer(inputs.size() - 1);
          inputIndex.put(input,indexInput);
      }

      int indexF = indexFrom.intValue();
      int indexT = indexTo.intValue();
      int indexI = indexInput.intValue();
      transitions.addValue(indexF,indexI,indexT);

  }

  public void print()
  {
      for(int i = 0; i < states.size(); i++)
          System.out.print(states.get(i)+" ");
      System.out.println();
      for(int i = 0; i < inputs.size(); i++)
          System.out.print(inputs.get(i)+" ");
      System.out.println();
      for(int i = 0; i < states.size(); i++)
          for(int j = 0; j < inputs.size(); j++)
              System.out.println(i+" "+j+" "+transitions.getValue(i,j));

  }

  public void minimize()
  {
      int statesSize = states.size();
      Distinguished = new boolean[statesSize][statesSize];
      Pending = new ArrayList[statesSize][statesSize];
      for(int i = 0; i < statesSize; i++)
          for(int j = 0; j < statesSize; j++)
              Pending[i][j] = new ArrayList();
      //initializaiton
      for(Enumeration e1 = finalStates.elements(); e1.hasMoreElements(); )
      {
          Integer indexP = (Integer)e1.nextElement();
          int intP = indexP.intValue();
          for(Enumeration e2 = nonFinalStates.elements(); e2.hasMoreElements(); )
          {
              Integer indexQ = (Integer)e2.nextElement();
              int intQ = indexQ.intValue();
              Distinguished[intP][intQ] = true;
              Distinguished[intQ][intP] = true;
          }
      }
      for(Enumeration e1 = finalStates.elements(); e1.hasMoreElements(); )
      {
          Integer indexP = (Integer)e1.nextElement();
          int intP = indexP.intValue();
          for(Enumeration e2 = finalStates.elements(); e2.hasMoreElements(); )
          {
              Integer indexQ = (Integer)e2.nextElement();
              int intQ = indexQ.intValue();
              Distinguished[intP][intQ] = false;
              Distinguished[intQ][intP] = false;
          }
      }
      for(Enumeration e1 = nonFinalStates.elements(); e1.hasMoreElements(); )
      {
          Integer indexP = (Integer)e1.nextElement();
          int intP = indexP.intValue();
          for(Enumeration e2 = nonFinalStates.elements(); e2.hasMoreElements(); )
          {
              Integer indexQ = (Integer)e2.nextElement();
              int intQ = indexQ.intValue();
              Distinguished[intP][intQ] = false;
              Distinguished[intQ][intP] = false;
          }
      }
      //iteration
      //F * F
      for(Enumeration e1 = finalStates.elements(); e1.hasMoreElements(); )
      {
          Integer indexP = (Integer)e1.nextElement();
          int intP = indexP.intValue();
          for(Enumeration e2 = finalStates.elements(); e2.hasMoreElements(); )
          {
              Integer indexQ = (Integer)e2.nextElement();
              int intQ = indexQ.intValue();
              if(intP == intQ)
                  continue;
              for(Enumeration e3 = inputIndex.elements(); e3.hasMoreElements(); )
              {
                  Integer indexI = (Integer)e3.nextElement();
                  int intI = indexI.intValue();
                  int s = transitions.getValue(intP, intI);
                  int t = transitions.getValue(intQ, intI);
                  if((s == -1 && t != -1) || (s != -1 && t == -1) || ((s != -1) && (t != -1) && (Distinguished[s][t] == true)))
                  {
                      recursiveMark(intP, intQ);
                      /*recursively mark all pair in Pending[intP][intQ] and
                      in all the lists Pending[s][t] for the pairs s,t that get marked here*/
                  }
              }
              //no pair s, t reachable by a single input symbol
              //from p, q is distinguished
              if(Distinguished[intP][intQ] == false)
              {
                  for(Enumeration e3 = inputIndex.elements(); e3.hasMoreElements(); )
                  {
                      Integer indexI = (Integer)e3.nextElement();
                      int intI = indexI.intValue();
                      int s = transitions.getValue(intP, intI);
                      int t = transitions.getValue(intQ, intI);
                      if((s != -1) && (t != -1) && (s != t))
                      {
                          ArrayList pendingList = Pending[s][t];
                          pendingList.add(new PendingStruct(intP,intQ));
                      }
                  }
              }
          }
      }
      //(Q - F) * (Q - F)
      for(Enumeration e1 = nonFinalStates.elements(); e1.hasMoreElements(); )
      {
          Integer indexP = (Integer)e1.nextElement();
          int intP = indexP.intValue();
          for(Enumeration e2 = nonFinalStates.elements(); e2.hasMoreElements(); )
          {
              Integer indexQ = (Integer)e2.nextElement();
              int intQ = indexQ.intValue();
              if(intP == intQ)
                  continue;
              for(Enumeration e3 = inputIndex.elements(); e3.hasMoreElements(); )
              {
                  Integer indexI = (Integer)e3.nextElement();
                  int intI = indexI.intValue();
                  int s = transitions.getValue(intP, intI);
                  int t = transitions.getValue(intQ, intI);
                  if((s == -1 && t != -1) || (s != -1 && t == -1) || ((s != -1) && (t != -1) && (Distinguished[s][t] == true)))
                  {
                      recursiveMark(intP, intQ);
                      /*recursively mark all pair in Pending[intP][intQ] and
                      in all the lists Pending[s][t] for the pairs s,t that get marked here*/
                  }
              }
              //no pair s, t reachable by a single input symbol
              //from p, q is distinguished
              if(Distinguished[intP][intQ] == false)
              {
                  for(Enumeration e3 = inputIndex.elements(); e3.hasMoreElements(); )
                  {
                      Integer indexI = (Integer)e3.nextElement();
                      int intI = indexI.intValue();
                      int s = transitions.getValue(intP, intI);
                      int t = transitions.getValue(intQ, intI);
                      if((s != -1) && (t != -1) && (s != t))
                      {
                          ArrayList pendingList = Pending[s][t];
                          pendingList.add(new PendingStruct(intP,intQ));
                      }
                  }
              }
          }
      }
      /*rebuild the graph*/
      for(int i = 0; i < statesSize; i++ )
      {
          for(int j = 0; j < statesSize; j++ )
          {
              if((i < j) && (Distinguished[i][j] == false))
              {
                  int deleteOne = i;
                  int replaceOne = j;
                  if(deleteOne == startIndex)
                  {
                    deleteOne = j;
                    replaceOne = i;
                  }
                  if(!deleteSet.contains(new Integer(deleteOne)))
                  {
                      //delete the redundant state's outgoing transitions
                      for(int indexI = 0; indexI <= transitions.maxColIndex; indexI ++)
                          transitions.addValue(deleteOne, indexI, -1);
                      //save the delete information
                      toDelete.add(new PendingStruct(deleteOne, replaceOne));
                      deleteSet.add(new Integer(deleteOne));
                      //delete the redundant state's ingoing transitions
                      for(int transRow = 0; transRow <= transitions.maxRowIndex; transRow++ )
                          for(int transCol = 0; transCol <= transitions.maxColIndex; transCol++ )
                              if(transitions.getValue(transRow, transCol) == deleteOne)
                                  transitions.addValue(transRow, transCol, replaceOne);
                      String deleteName = (String)states.get(deleteOne);
                      if(finalStates.contains(deleteName))
                          finalStates.remove(deleteName);
                      else if(nonFinalStates.contains(deleteName))
                          nonFinalStates.remove(deleteName);
                  }
              }
          }
      }
  }

  private void recursiveMark(int p, int q)
  {
      Distinguished[p][q] = true;
      ArrayList pendingList = Pending[p][q];
      for(int i = 0; i < pendingList.size(); i++)
      {
          PendingStruct struct = (PendingStruct)pendingList.get(i);
          recursiveMark(struct.P, struct.Q);
      }
  }

  public void output(BufferedWriter writer)
  {
      try
      {
          writer.write("Start State:\t");
          writer.write(startStateName);
          writer.newLine();
          writer.newLine();
          writer.write("Final States:\t");
          for(Enumeration e = finalStates.elements(); e.hasMoreElements();)
          {
              Integer i = (Integer)e.nextElement();
              writer.write((String)states.get(i.intValue()));
              writer.write("\t");
          }
          writer.newLine();
          writer.newLine();
          writer.write("Transitions:\n\n");
          for(int row = 0; row <= transitions.maxRowIndex; row++)
          {
              for(int col = 0; col <= transitions.maxColIndex; col++)
              {
                  if(transitions.getValue(row, col) != -1)
                  {
                      writer.write((String)states.get(row));
                      writer.write("\t");
                      int value = transitions.getValue(row, col);
                      writer.write((String)states.get(value));
                      writer.write("\t");
                      writer.write((String)inputs.get(col));
                      writer.newLine();
                  }
              }
          }
          writer.newLine();
          writer.write("------------------------------------------------------------\n");
          writer.write("Maps:\n\n");
          if(toDelete.size() == 0)
              writer.write("None.");
          else
          {
              for(int i = 0; i < toDelete.size(); i++)
              {
                  PendingStruct struct = (PendingStruct)toDelete.get(i);
                  int deleteOne = struct.P;
                  int replaceOne = struct.Q;
                  writer.write((String)states.get(replaceOne));
                  writer.write("\t");
                  writer.write("replaces");
                  writer.write("\t");
                  writer.write((String)states.get(deleteOne));
                  writer.write("\n");
              }
          }
          writer.flush();
      }
      catch(IOException e)
      {
        System.out.println(e.getMessage());
      }
  }

}