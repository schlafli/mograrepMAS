
distinct([],[]).
distinct([H|T],C) :- .member(H,T) & distinct(T,C).
distinct([H|T],[H|C]) :- distinct(T,C).


intersection([X|Xt],Y,[X|Z]):- .member(X,Y) & intersection(Xt,Y,Z). 
intersection([X|Xt],Y,Z):- not .member(X,Y) & intersection(Xt,Y,Z). 
intersection([], Y, []).

reverse([X|Y],Z,W) :- reverse(Y,[X|Z],W).
reverse([],X,X).

