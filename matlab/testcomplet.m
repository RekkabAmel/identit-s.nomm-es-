I=imread('0001_m_l_01.jpg');
mapping=getmapping(8,'u2');

H1=lbp(I,1,8,mapping,'h'); %LBP histogram in (8,1) neighborhood using uniform patterns

figure;
H3=lbp(I);

figure;
subplot (1,2,1),stem (H1);
subplot (1,2,2),stem (H3);

SP=[-1 -1; -1 0; -1 1; 0 -1; -0 1; 1 -1; 1 0; 1 1];

I2=lbp(I,SP,0,'i'); %LBP code image using sampling points in SP and no mapping. Now H2 is equal to histogram of I2.

figure;
subplot(131);imshow(I);
subplot(132);imshow(I2);
subplot(133);imshow(lbp(I,1,8,mapping,'i'));

repertoire = 'C:\projetmatleb\0001';
extension = 'jpg';

mesFichiers = ls(fullfile(repertoire,['*.',extension]));
  %mesFichiers donne la liste complète des fichiers avec leurs noms complets
  %sous forme d'une chaine de caractères
  %Transformer mesFichiers en tableau de cellules :
mesFichiers=cellstr(mesFichiers);

 % ou bien 
 % mesFichiers = dir(fullfile(repertoire,['*.',extension]));
     % mesFichiers est alors une structure
     % Transformer mesFichiers en tableau de cellules :
 % mesFichiers = {mesFichiers(:).name};

for lF=1:length(mesFichiers)
   fichCur=mesFichiers{lF};
   J=imread('fichCur');
   H2=lbp(J,1,8,mapping,'h');
   H4=lbp(J);
   J2=lbp(J,SP,0,'j');
   D= pdist2(H3,H4,'hamming');
   if (D < 0.3)
     disp('individu identifié avec succée');
     disp(D);

     figure;
     subplot(1,2,1),stem(H2);
     subplot(1,2,2),stem(H4);
      
     figure;
     subplot(131);imshow(J);
     subplot(132);imshow(J2);
     subplot(133);imshow(lbp(J,1,8,mapping,'j'));
    
   else 
     disp('individu inconnu');
   end
         
end


