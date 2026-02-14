world_population=7770000000;%7.77e9
patients=342777; %those who have the virus
P_covid=patients/world_population; % P(covid) prior probability
fprintf("P(covid)=%f\n",P_covid);
P_ncovid=1-P_covid; %P(~covid) prior probability
fprintf("P(~covid)=%f\n",P_ncovid);
P_pos_covid=0.99; %P(test+|covid) is the likelihood
P_pos_ncovid=0.01;%P(test+|~covid) 
P_covid_pos= (P_pos_covid*P_covid)/(P_covid*P_pos_covid+P_ncovid*P_pos_ncovid); 
% P(test+)=P(covid)*P(test+|covid)+P(!covid)*P(test+|!covid)->evidence
% P(covid|test+) is the posterior probability that we calculate here.
fprintf("P(covid|test+)=%f\n",P_covid_pos);


