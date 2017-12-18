import numpy as np
import re
import matplotlib.pyplot as plt
from sklearn.decomposition import TruncatedSVD as SVD
from sklearn.manifold import TSNE as TSNE
import os


#PCA Implementation
class PCA(object):
    def __init__(self):
        self.name = ""
        self.cols = 0
        self.labels = {}
        self.colors = {0: "red", 1: "yellow", 2: "blue", 3: "green", 4: "black", 5: "cyan", 6: "magenta", 7: "white"}

    def sort(self,evals,evecs):
        evecs = evecs.T
        n = len(evals)
        dict = {}
        count = 0
        for i in range(0,n):
            dict[count] = [[evals[i],evecs[i]]]
            count += 1

        l = sorted(dict.values(),key = lambda v :v[0],reverse=True)
        return l

    def plot(self,data,labels,clabels,name1,name2):
        fig = plt.figure(figsize=(10,10))
        ax = fig.add_subplot(1, 1, 1, facecolor="1.0")
        xlabels = []
        for i in range(0,len(data)):
            if not labels[i] in xlabels:
                ax.scatter(data[i][0],data[i][1],alpha = 0.8, c = self.colors.get(clabels.get(labels[i])), edgecolors = 'none',s = 30, label = labels[i])
                xlabels.append(labels[i])
            else:
                ax.scatter(data[i][0], data[i][1], alpha=0.8, c=self.colors.get(clabels.get(labels[i])), edgecolors='none', s=30)

        plt.title(name2+"  ("+name1+")")
        plt.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05),fancybox=True, shadow=True, ncol=5)
        fname = "output/"+name1+"_"+name2+".jpg"
        plt.savefig(fname)

    def pca(self,data,labels):
        #Calculate Mean
        mean = data.mean(axis=0)

        #Subtract mean from data
        data = np.subtract(data,mean)

        #Calculate Covariance
        cov = np.cov(data.T)

        #Calculate Eigen Values and Eigen Vectors
        evals, evecs = np.linalg.eig(cov)

        #Get Sorted Tuples
        tuples = self.sort(evals,evecs)
        # Choosing top 2 eigen vectors
        tuples = tuples[0:2]

        #Get the projection matrix
        projection_matrix = np.zeros((2,self.cols))
        count = 0
        for i in tuples:
            for k,v in i:
                for j in range(0,self.cols):
                    projection_matrix[count][j] = v[j]
            count += 1
        projection_matrix = projection_matrix.T

        #Get new_data
        pca_data = data.dot(projection_matrix)
        self.plot(pca_data,labels,self.labels,self.name,"PCA")

        #Using package TruncatedSVD
        svd = SVD(n_components=2)
        svd_data = svd.fit_transform(data)
        self.plot(svd_data, labels, self.labels,self.name,"SVD")

        tsne = TSNE(n_components=2,init="pca",learning_rate=100)
        tsne_data = tsne.fit_transform(data)
        self.plot(tsne_data, labels, self.labels,self.name,"tSNE")


directory = os.path.normpath("input")
for subdir, dirs, files in os.walk(directory):
    for filename in files:
        if filename.endswith(".txt"):
            ob = PCA()
            ob.name = filename
            #Reads Dimensions
            num_rows = sum(1 for line in open("input/"+filename))
            with open("input/"+filename) as f:
                for i in f:
                    n = re.split(r'\t+', i)
                    ob.cols = len(n) - 1
                    break

            #Reads Data
            data = np.zeros((num_rows,ob.cols))
            labels = []
            with open("input/"+filename) as f:
                count = 0
                clabel = 0
                for i in f:
                    n = re.split(r'\t+', i)
                    data[count] = list(map(float,n[:-1]))
                    labels.append(n[-1])
                    if not ob.labels.__contains__(n[-1]):
                        ob.labels[n[-1]] = clabel
                        clabel += 1
                    count += 1

            ob.pca(data,labels)

print("Done!")




