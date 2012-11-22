package minimization;

/**
 * <p>Title: Minimization</p>
 * <p>Description: Minimization of deterministic finite state machines</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: BLRC</p>
 * @author Xu, Shun
 * @version 1.0
 */

import java.lang.*;

public class Int2DArray {


  int[][] array;
  int m_capacity = 100;
  int m_increment = 50;
  int maxRowIndex = 0;
  int maxColIndex = 0;

  public Int2DArray()
  {
      array = new int[m_capacity][m_capacity];
      for(int row = 0; row < m_capacity; row++)
        for(int col = 0; col < m_capacity; col++)
          array[row][col] = -1;
  }

  public Int2DArray(int capacity, int increment)
  {
      m_capacity = capacity;
      m_increment = increment;
      array = new int[m_capacity][m_capacity];
      for(int row = 0; row < m_capacity; row++)
        for(int col = 0; col < m_capacity; col++)
          array[row][col] = -1;
  }

  public void addValue(int row, int col, int value)
  {
      int newCapacity = m_capacity;
      if(row >= m_capacity || col >= m_capacity)
      {
          int max = row;
          if(col > row)
            max = col;
          while(newCapacity <= max)
          {
              newCapacity += m_increment;
          }
          int[][] newArray = new int[newCapacity][newCapacity];
          for(int i = 0; i < m_capacity; i++)
            for(int j = 0; j < m_capacity; j++)
              newArray[i][j] = array[i][j];
          for(int i = m_capacity; i < newCapacity; i++)
            for(int j = 0; j < newCapacity; j++)
              newArray[i][j] = -1;
          array = newArray;
          m_capacity = newCapacity;
      }
      array[row][col] = value;
      if(row > maxRowIndex)
          maxRowIndex = row;
      if(col > maxColIndex)
          maxColIndex = col;
  }

  public int getValue(int row, int col) throws ArrayIndexOutOfBoundsException
  {
      if(row >= m_capacity || col >= m_capacity)
        throw new ArrayIndexOutOfBoundsException();
      return array[row][col];
  }

}