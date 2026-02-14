%Kostas Konstantakis, A.M: 3219
%csd3219@csd.uoc.gr

% Benchmark
clc;
clear all;

% header: img = 16bytes, labels = 8 bytes
itrain = fread(fopen('train-images-idx3-ubyte')); itrain = itrain(17 : end); itrain = reshape(itrain ./ 255, 28, [])';
ltrain = fread(fopen('train-labels-idx1-ubyte')); ltrain = ltrain(9 : end);

itest = fread(fopen('train-images-idx3-ubyte')); itest = itest(17 : end); itest = reshape(itest ./ 255, 28, [])';
ltest = fread(fopen('train-labels-idx1-ubyte')); ltest = ltest(9 : end);

f = waitbar(0, 'Running benchmark...');

correct = 0;
for i = 1 : 200 % #test images
    d = [];
    for j = 1 : 5000 % #train images
        d = [d; mydist(itest(1 + (i-1) * 28 : i * 28, 1 : 28), itrain(1 + (j-1) * 28 : j * 28, 1 : 28))];
    end
    
    [~, I] = sort(d);
    l = mode(ltrain(I(1 : 5)));
    
    if l == ltest(i)
        correct = correct + 1;
    end
    
    waitbar(i / 200, f);
end

correct / 2 % accuracy in percent
close(f);


% TODO: Implement your own custom distance function between two images
% @param x: 28x28 pixel gray-scale floating point image (0 <= px <= 1)
% @param y: 28x28 pixel gray-scale floating point image (0 <= px <= 1)
% @return: The 'distance' between x and y, a measure of
% similarity/class-belonging
function d = mydist(x, y)
    % TODO: Improve this naive implementation
    %p=4;
    %d = sum(sum(abs(x - y).^p).^(1/p)); %p=2, 91 p=3 or 4->88.5000
    
    %d = sum(max(abs(x - y))); %Chess-board distance=88.5000
    
    d = sum(sum(abs(x.^(1/2) - y.^(1/2)))); %same result as the Manhattan distance
    
    %d = sum(sum(abs(x - y))); %Manhattan distance=91.5000
     %d = sum(sum(abs(2*x.^3 - y))); %82.5000
     % d = sum(sum(abs(3*x.^3 - y))); %84
     % d = sum(sum(abs(2*x.^2 - y))); %86.5000
    % d = sum(sum(abs(3*x.^2 - y))); %87
    %d =sum(sum(abs(4*x.^3 - y)));  %84
    %d = sum(sum(abs(x.^2 - y*(1/2)))); %86.5000
    %d=sum(max(abs(x.^(1/5) - y))); %73.5000
    %d=sum(max(abs(x.^(1/4) - y))); %77.5000
    %d=sum(max(abs(x.^(1/3) - y))); %79.5000
    %d=sum(max(abs(x.^(1/2) - y))); %81
    %d = sum(sum(abs(x.^(1/2) - y))); %89
    %d = sum(sum(abs(x - y.^(1/2)))); %83
    %d = sum(sum(abs(x - y.^(1/3)))); %74.5000
    %d = sum(sum(abs(x - y.^(1/4)))); %73.5000
     %d= sum(sum(abs(2*x - y)));%83.5000
     %d = sum(sum(abs(2*x.^(1/2) - y.^(1/2)))); %83
     %d = sum(sum(abs(2*x.^(1/2) - y))); %74.5000
     %d = sum(sum(abs(2*x.^3 - y))); %82.5000
    %d = sum(max(sum(x - y).^12)); %78, агагагагагагаг!
    %Ok, it's pretty visible that this function "prefers" even numbers! :P 
    %d = sum(max(sum(x - y).^6)); %78
    %d = sum(max(sum(x - y).^4)); %78
    %d = sum(max(sum(x - y).^2)); %78
    %d = sum(sum(abs(x - y.^3))); %83
    %d = sum(sum(abs(x - y.^(1/3)))); %74.5000
    %d = sum(sum(abs(x - y.^(1/2)))); %83
    %d = sum(sum(abs(x - y.^2))); %86.5000
    %d = sum(sum(abs(x.^(1/5) - y))); %79
    %d = sum(sum(abs(x.^(1/4) - y))); %84.5000
    %d = sum(sum(abs(x.^(1/3) - y))); %87.5000
    %d = sum(sum(abs(x.^(1/2) - y))); %89
    %d = sum(sum(abs(x.^2 - y))); %78.5000
    %d = sum(sum(abs(x^3 - y^3))); %85.5000
    %d = sum(sum(abs(x^4 - y^4))); %83.5000
    %d = sum(sum(abs(x^5 - y^5))); %84
    %d = sum(sum(abs(x^6 - y^6))); %85
    %d = sum(sum(abs(x^7 - y^7)));%84.5000
    %d = sum(sum(abs(x.^7 - y.^7))); %80.5000
    %d = sum(sum(abs(x.^6 - y.^6))); %83
    %d = sum(sum(abs(x.^5 - y.^5))); %83.5000
    %d = sum(sum(abs(x.^4 - y.^4))); %84
    %d = sum(sum(abs(x.^3 - y.^3))); %85
    %d = sum(sum(abs(x.^(1/6) - y.^(1/6)))); %88
    %d = sum(sum(abs(x.^(1/5) - y.^(1/5)))); %89
    %d = sum(sum(abs(x.^(1/4) - y.^(1/4)))); %89.5000
    %d = sum(sum(abs(x.^(1/3) - y.^(1/3)))); %89.5000
    %d = sum(sum(abs(x.^2 - y.^2))); %88.5000
    %d = sum(sum(abs(x^2 - y^2))); %87.5000
    %d = sum(sum(abs(x - y).^4)); %87.5000
    %d = sum(sum(abs(x - y).^3)); %91
    %d=sum(max(abs(x - y)^2)); %90
    %d=sum(sum((x-y).^8)); %82
    %d=sum(sum((x-y).^7)); %3.5000
    %d=sum(sum((x-y).^6)); %85, decent!
    %d=sum(sum((x-y).^5)); %2.5000, LOL!
    %d=sum(sum((x-y).^4)); %87.5000
    %d=sum(sum((x-y).^3)); %2, AHAHAHAHAHAHAH 
    %d=sum(sum((x-y).^2)); %same as Euclidean distance
    %d=sum(max(abs(x - y).^(1/3))); %80.5000
    %d=sum(max(abs(x - y).^(1/2))); %78
    %d=sum(max(abs(x - y).^4)); %77.5000
    %d=sum(max(abs(x - y)^4)); %88
    %d=sum(max(abs(x - y)^3)); %88
    %d=sum(max(abs(x - y).^3)); %77.5000
    %d=sum(max(abs(x - y).^2)); %77
    %d=sum(max(abs(x - y))); %Chebyshev distance=77.5000
    %d=sum(sum((x - y).^2))=sum(sum(abs(x - y).^2)); %Euclidean distance=89.5000 
    %d=sum(sum(x-y))); %1.500 :( 
end