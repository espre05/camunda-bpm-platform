/*
 * Copyright © 2012 - 2018 camunda services GmbH and various authors (info@camunda.com)
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
package org.camunda.bpm.engine.test.bpmn.servicetask;

import java.io.Serializable;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.test.Deployment;


/**
 *
 * @author Daniel Meyer
 */
public class ServiceTaskVariablesTest extends PluggableProcessEngineTestCase {
  
  static boolean isNullInDelegate2;
  static boolean isNullInDelegate3;
  
  public static class Variable implements Serializable {
    private static final long serialVersionUID = 1L;
    public String value;    
  }
  
  public static class Delegate1 implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
      Variable v = new Variable();
      execution.setVariable("variable", v);
      v.value = "delegate1";
    }
    
  }
  
  public static class Delegate2 implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
      Variable v = (Variable) execution.getVariable("variable");
      synchronized (ServiceTaskVariablesTest.class) {
        // we expect this to be 'true'
        isNullInDelegate2 = (v.value != null && v.value.equals("delegate1"));         
      }
      v.value = "delegate2";      
    }
    
  }
  
  public static class Delegate3 implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
      Variable v = (Variable) execution.getVariable("variable");
      synchronized (ServiceTaskVariablesTest.class) {
        // we expect this to be 'true' as well
        isNullInDelegate3 = (v.value != null && v.value.equals("delegate2"));  
      }
    }
    
  }
  
  @Deployment
  public void testSerializedVariablesBothAsync() {
    
    // in this test, there is an async cont. both before the second and the
    // third service task in the sequence
    
    runtimeService.startProcessInstanceByKey("process");
    waitForJobExecutorToProcessAllJobs(10000);
    
    synchronized (ServiceTaskVariablesTest.class) {
      assertTrue(isNullInDelegate2);
      assertTrue(isNullInDelegate3); 
    }
  }

  @Deployment
  public void testSerializedVariablesThirdAsync() {
    
    // in this test, only the third service task is async
        
    runtimeService.startProcessInstanceByKey("process");
    waitForJobExecutorToProcessAllJobs(10000);
    
    synchronized (ServiceTaskVariablesTest.class) {
      assertTrue(isNullInDelegate2); 
      assertTrue(isNullInDelegate3); 
    }
    
  }

}

