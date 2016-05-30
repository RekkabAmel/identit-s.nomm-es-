testLBP.m1/ 1
I=imread('0001_m_l_01.jpg');
mapping=getmapping(8,'u2');
H1=lbp(I,1,8,mapping,'h'); 
%LBP histogram in (8,1) neighborhood using uniform patternsfigure;subplot(2,1,1),stem(H1);
H2=lbp(I);
subplot(2,1,2),stem(H2);
SP=[-1 -1; -1 0; -1 1; 0 -1; -0 1; 1 -1; 1 0; 1 1];
I2=lbp(I,SP,0,'i'); 
%LBP code image using sampling points in SP and no mapping. Now H2 is equal to histogram of I2.                 
figure;
subplot(131);
imshow(I);
subplot(132);
imshow(I2);
subplot(133);
imshow(lbp(I,1,8,mapping,'i'));
I=imread('0001_m_l_01.jpg');
J=imread('0001_m_1_02.jpg');

mapping=getmapping(8,'u2');

H1=lbp(I,1,8,mapping,'h'); %LBP histogram in (8,1) neighborhood using uniform patterns

figure;
H2=lbp(J,1,8,mapping,'h');

figure;

subplot(1,4,1),stem(H1);
subplot(1,4,2),stem(H2);


H3=lbp(I);

subplot(1,4,3),stem(H3);
H4=lbp(J);
subplot(1,4,4),stem(H4);




SP=[-1 -1; -1 0; -1 1; 0 -1; -0 1; 1 -1; 1 0; 1 1];

I2=lbp(I,SP,0,'i'); %LBP code image using sampling points in SP and no mapping. Now H2 is equal to histogram of I2.
J2=lbp(J,SP,0,'j');



figure;
subplot(131);imshow(I);
subplot(132);imshow(I2);
subplot(133);imshow(lbp(I,1,8,mapping,'i'));
figure;
subplot(131);imshow(J);
subplot(132);imshow(J2);
subplot(133);imshow(lbp(J,1,8,mapping,'j'));
D= pdist2(H3,H4,'hamming');
disp(D);
if (D < 0.2)
    disp('individu identifié avec succée');
else 
    I=imread('0001_m_l_01.jpg');
J=imread('0001_m_1_02.jpg');

mapping=getmapping(8,'u2');

H1=lbp(I,1,8,mapping,'h'); %LBP histogram in (8,1) neighborhood using uniform patterns

figure;
H2=lbp(J,1,8,mapping,'h');

figure;

subplot(1,4,1),stem(H1);
subplot(1,4,2),stem(H2);


H3=lbp(I);

subplot(1,4,3),stem(H3);
H4=lbp(J);
subplot(1,4,4),stem(H4);




SP=[-1 -1; -1 0; -1 1; 0 -1; -0 1; 1 -1; 1 0; 1 1];

I2=lbp(I,SP,0,'i'); %LBP code image using sampling points in SP and no mapping. Now H2 is equal to histogram of I2.
J2=lbp(J,SP,0,'j');



figure;
subplot(131);imshow(I);
subplot(132);imshow(I2);
subplot(133);imshow(lbp(I,1,8,mapping,'i'));
figure;
subplot(131);imshow(J);
subplot(132);imshow(J2);
subplot(133);imshow(lbp(J,1,8,mapping,'j'));
D= pdist2(H3,H4,'hamming');
disp(D);
if (D < 0.2)
    disp('individu identifié avec succée');
else 
    disp('individu inconnu');
end
end
