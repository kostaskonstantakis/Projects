%y = binopdf(x,n,p)
N=30;
L=12;
R=N-L;
F=8;
B=N-F;
FL=F+L;

x=0:1; % 0<=x<=1
y=0:1; % 0=<y=<1
pL=L/N; %Probability of the ball landing left of the coin is 12/30
pR=R/N; %Probability of the ball landing right of the coin is 18/30
pF=F/N; %Probability of the ball landing in front of the coin is 8/30
pB=B/N; %Probability of the ball landing behind the coin is 22/30

disp("Binomial distribution pdf for the ball landing left of the coin");
bdxl=binopdf(x,N,pL);
fprintf("bdx=%f\n",bdxl);
bdyl=binopdf(y,N,pL);
fprintf("bdy=%f\n",bdyl);

disp("Binomial distribution pdf for the ball landing in front of the coin");
bdxf=binopdf(x,N,pF);
fprintf("bdx=%f\n",bdxf);
bdyf=binopdf(y,N,pF);
fprintf("bdy=%f\n",bdyf);



%{
%Multiline comment
%}
pFL=FL/N;

disp("Now, Binomial distribution pdf for the ball landing either left or in front of the coin");
bdxfl=binopdf(x,N,pFL);
fprintf("bdxfl=%f\n",bdxfl);
bdyfl=binopdf(y,N,pFL);
fprintf("bdyfl=%f\n",bdyfl);

%The final probability we want is this:
posterior= (bdxl+bdyf)*pFL/((bdxl+bdyf)*(20/30)+(1-pFL)*(1-(bdxl+bdyf))); 
%Implementation of Bayes rule
fprintf("Posterior probablity that a ball will land left or in front of the coin=%f\n",posterior);


