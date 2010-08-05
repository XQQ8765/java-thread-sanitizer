/* Copyright (c) 2010 Google Inc.                                                                                                                             
 *                                                                                                                                                            
 * Licensed under the Apache License, Version 2.0 (the "License");                                                                                            
 * you may not use this file except in compliance with the License.                                                                                           
 * You may obtain a copy of the License at                                                                                                                    
 *                                                                                                                                                            
 *     http://www.apache.org/licenses/LICENSE-2.0                                                                                                             
 *                                                                                                                                                            
 * Unless required by applicable law or agreed to in writing, software                                                                                        
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                          
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                                                                                   
 * See the License for the specific language governing permissions and                                                                                        
 * limitations under the License.                                                                                                                             
 */

package org.jtsan.writers;

import org.jtsan.EventType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * This class if usefull in debug to check equals of bin and str writers output.
 *
 * @author: Sergey Vorobyev
 */
public class BinAndStrEventWriter implements EventWriter {

  BinaryEventWriter binWriter;
  StringEventWriter strWriter;

  // bin output write to outputStream, that set by Agent
  // but str output write to this file.
  public static final String STR_OUT_FILE = "jtsan.events.debug";

  public BinAndStrEventWriter() {
    binWriter = new BinaryEventWriter();
    strWriter = new StringEventWriter();
    try {
      strWriter.setOutputStream(new FileOutputStream(STR_OUT_FILE));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void setOutputStream(OutputStream outputStream) {
    binWriter.setOutputStream(outputStream);
  }

  public void writeEvent(EventType type, long tid, long pc, long address, long extra) {
    synchronized (this) {
      binWriter.writeEvent(type, tid, pc, address, extra);
      strWriter.writeEvent(type, tid, pc, address, extra);
    }
  }

  public void writeCodePosition(long pc, String descr) {
    synchronized (this) {
      binWriter.writeCodePosition(pc, descr);
      strWriter.writeCodePosition(pc, descr);
    }
  }

  public void writeComment(String str, long pc) {
    synchronized (this) {
      binWriter.writeComment(str, pc);
      strWriter.writeComment(str, pc);
    }
  }

}
