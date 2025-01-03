# SYSC 3303 - Iteration #4

## Group 2 Members

- Tina Cao, 101158711
- Francesca Siconolfi, 101148917
- Russell Radko, 101187309
- Haozhe Li, 101148446
- Basma Aboushaer, 101186291


## Breakdown of Responsibilities 

- Basma: Timing Diagrams
- Francesca: Timing Diagrams
- Haozhe: Elevator and JUnit test 
- Russell: Add Error injection and fault handling to scheduler and elevator
- Tina: Readme, revise UML

## Files Included in this Iteration

- *Config.java* - store values
- *EDoorsClosed.java* - Elevator State, close door
- *EDoorsOpen.java* - Elevator State, open door
- *EMovingDown.java* -Elevator State, moving down
- *EMovingUp.java* - Elevator State, moving up
- *Elevator.java* - Elevator source file
- *ElevatorContext.java* - Receive packet, set elevator states and send packet
- *ElevatorState.java* - Elevator state
- *ElevatorSystemGUI.java* - Elevator GUI, labels of status
- *ElevatorTimer.java* - Elevator timer that includes the direction and context
- *Floor.java* - Floor source file
- *InfoPacket.java* - Buffer
- *Queue.java* - Queue for elevator requests
- *SFloorArrivalState.java* - subtype of states
- *SFloorNotificationState.java* - subtype of states
- *SFloorRequestState.java* - subtype of states
- *SHandleInternalRequestState.java* - subtype of states
- *SScheduleRequestState.java* - subtype of states
- *Scheduler.java* - Scheduler source file
- *SchedulerContext.java* - Receive packet, set scheduler states and send packet
- *SchedulerState.java* - Scheduler state
- *UDPThread.java* - handle UDP packets received on a socket and process them

Test files
- *UDPThreadTest.java* - Tester for UDPThread
- *SchedulerTest.java* - Tester for Scheduler
- *SchedulerStateTest.java* - Tester for SchedulerState
- *InfoPacketTest.java* - Tester for InfoPacket
- *SchedulerContextTest.java* - Tester for SchedulerContext
- *ElevatorTest.java* - Tester for Elevator
- *ElevatorStateTest.java* - Tester for ElevatorState
- *ElevatorContextTest.java* - Tester for ElevatorContext
- *QueueTest.java* - Tester for Queue
- *FloorTest.java* - Tester for Floor

Other Files
- *I1_ClassDiagram.png*: The class diagram of the system
- *I1_SeqDiagram_1.png*: The sequence diagram of the interactions between the scheduler and the floor (one iteration)
- *I1_SeqDiagram_2.png*: The sequence diagram of the interactions between the scheduler and the elevator (one iteration)
- *I2_SchedulerStateMachine.png*: The state machine diagram for Scheduler
- *I2_ElevatorStateMachine.png*: The state machine diagram for Elevator
- *I2_SchedulerClassDiagram.png*: The class diagram for Scheduler
- *I2_ClassDiagram.png*: Updated class diagram of the system
- *I3_ClassDiagram.png*: Updated class diagram of the system

 

## Set-up Instructions

Run system: run source files in intellij
Run tests: run test files and check for errors
