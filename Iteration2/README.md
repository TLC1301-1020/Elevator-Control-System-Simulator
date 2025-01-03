# SYSC 3303 - Iteration #2

## Group 2 Members

- Tina Cao, 101158711
- Francesca Siconolfi, 101148917
- Russell Radko, 101187309
- Haozhe Li, 101148446
- Basma Aboushaer, 101186291


## Breakdown of Responsibilities 

- Basma: Design Scheduler state diagram + UML class diagrams for the states
- Francesca: Develop Elevator state diagram, UML class diagrams for the states
- Haozhe: Code for Elevator state machine
- Russell: Code Scheduler state machine
- Tina: Update unit tests, README and UML overall class diagram

## Files Included in this Iteration

**From Iteration #1, updated**

Source Files
- *Floor.java*: Client in the system, read in events and sends input to the Scheduler
- *Elevator.java*: Client in the system, make calls to the Scheduler, send data to the Scheduler 
- *InfoPacket.java*: Contains messages to be passed between subsystems
- *Queue.java*: Manage requests, data
- *Scheduler.java*: Server in the system, send data to the Floor
- *Main.java*: Main execution file
  
Test Files
- *SchedulerTest.java*: Test file for Scheduler
- *ElevatorTest.java*: Test file for Elevator
- *QueueTest.java*: Test file for Queue
- *FloorTest.java*: Test file for Floor
  
Other Files
- *I1_ClassDiagram.png*: The class diagram of the system
- *I1_SeqDiagram_1.png*: The sequence diagram of the interactions between the scheduler and the floor (one iteration)
- *I1_SeqDiagram_2.png*: The sequence diagram of the interactions between the scheduler and the elevator (one iteration)


**From Iteration #2**

Source Files
- *SchedulerState.java*: Represents the state of the Scheduler
- *SchedulerContext.java*: Current state of the Scheduler, manages state subtypes
- *SIdleState.java*: State subtype SchedulerState, receives and processes Floor requests
- *SFloorArrivalState.java*: State subtype for SchedulerState, arrives at a floor
- *SSendCommandState.java*: State subtype for SchedulerState, ready to send commands
- *SFloorRequestState.java*: State subtype for SchedulerState, floor request received

Test Files
- *SchedulerStateTest.java*: Test file for SchedulerState
- *ElevatorStateTest.java*: Test file for ElevatorState
  
Other Files
- *README.txt*: Explanation of details of iteration #2
- *I2_SchedulerStateMachine.png*: The state machine diagram for Scheduler
- *I2_ElevatorStateMachine.png*: The state machine diagram for Elevator
- *I2_SchedulerClassDiagram.png*: The class diagram for Scheduler
- *I2_ClassDiagram.png*: Updated class diagram of the system
- *input.txt*: Handle input for iteration #2

## Set-up Instructions
- Run the main file for main program execution
- Run the unit test files to individually test classes and functionalities

