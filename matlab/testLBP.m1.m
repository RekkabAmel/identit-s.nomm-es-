I=imread('C:\projetmatleb\0001\0001_m_l_08.jpg');
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

rep = 'C:\projetmatleb\0002';
ext = '*.jpg';
chemin=fullfile(rep,ext);
list=dir(chemin);

%  %mesFichiers donne la liste complète des fichiers avec leurs noms complets
  %sous forme d'une chaine de caractères
  %Transformer mesFichiers en tableau de cellules :
%mesFichiers=cellstr(mesFichiers);

 % ou bien 
 % mesFichiers = dir(fullfile(repertoire,['*.',extension]));
     % mesFichiers est alors une structure
     % Transformer mesFichiers en tableau de cellules :
 % mesFichiers = {mesFichiers(:).name};

for n=1:numel(list)
    J=imread(fullfile(rep,list(n).name),ext(3:end));
   H2=lbp(J,1,8,mapping,'h');
   H4=lbp(J);
   J2=lbp(J,SP,0,'j');
   D= pdist2(H3,H4,'hamming');
   if (D < 0.3)
       var = 1;
   break;
   else
       var = 0;
   end
end
  if var == 1  
     disp('individu identifié avec succée');
     disp('valide');
     figure;
     subplot(1,2,1),stem(H2);
     subplot(1,2,2),stem(H4);
      
     figure;
     subplot(131);imshow(J);
     subplot(132);imshow(J2);
     subplot(133);imshow(lbp(J,1,8,mapping,'j'));
  else 
     disp ('la difference est de:')
     disp (D)
     disp('individu inconnu');
     
  end 


