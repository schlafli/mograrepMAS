/*
 * This is the contractor behaviour
 * 
 * 
 * 
 * 
 */

{include("utils.asl")}



getTasks(Plan, Tasks):- getTasks_1(Plan, [], TasksTmp) & reverse(TasksTmp, [], Tasks).

getTasks_1([], P, P).
getTasks_1([[_|[Task]]|T], Tmp, Tasks):- getTasks_1(T, [Task|Tmp], Tasks).


planID(0). //initial planID
running_plans(0). //number of currently running plans

all_team_members_responded(PlanID)
:- 	.count(aok(PlanID,_),OK) &
	.count(problems(PlanID,_,_),Prob) &
	team_ass(PlanID, Team) &
	distinct(Team, Individuals) &
	.length(Individuals, Ln) &
	Ln = (OK+Prob).



all_proposals_received(PlanID):-
	.count(propose(PlanID,_,_,_), NO) &           // number of proposes received
    .count(refuse(PlanID,_,_), NR) &
     total_msgs(PlanID, Tot)  &
     Tot = (NO+NR).


!contractor_start.

+!contractor_start: //Do the initial registration
	true
	<-
	//************
	//FILE PREFIX
	//************
	
	//env.start_log(def);
	
	
	?plays(directory, Dir); //Find out who does the contractor directory
	.my_name(Me);
	.send(Dir, tell, plays(contractor, Me));//advertise self as being contractor
	.

@ipidc [atomic]
+?newPlanId(ID): true
	<-
	?planID(N);
	-planID(N);
	ID = N + 1;
	+planID(ID);
	.
	

