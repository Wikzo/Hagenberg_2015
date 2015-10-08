news1 = scan("news1.txt", what="character")
news2 = scan("news2.txt", what="character")
news3 = scan("news3.txt", what="character")
news4 = scan("news4.txt", what="character")
news5 = scan("news5.txt", what="character")

#list
#union
#is.element

v = c(0);

# put 1 extra ID column
# article 1

length = length(news1);
a = matrix(v, length, 1)
news1_matrix = matrix(c(news1, a), length, 2);

# article 2
length = length(news2);
a = matrix(v, length, 1)
news2_matrix = matrix(c(news2, a), length, 2);

# article 3
length = length(news3);
a = matrix(v, length, 1)
news3_matrix = matrix(c(news3, a), length, 2);

# article 4
length = length(news4);
a = matrix(v, length, 1)
news4_matrix = matrix(c(news4, a), length, 2);

# article 5
length = length(news5);
a = matrix(v, length, 1)
news5_matrix = matrix(c(news5, a), length, 2);

newsArticles = list(news1_matrix, news2_matrix, news3_matrix, news4_matrix, news5_matrix);

#print(newsArticles[[c(1,1)]]);
#a = newsArticles[1]

#newsArticles[[2]][1,2];

#a = get(newsArticles[1]);

#g = get(newsArticles[1])

#newsArticles[[1,]]

#news1_matrix[1,2]

for (i in 1:1 ) # loop through 5 articles
{
  
  for (j in 1:length(newsArticles[[i]]) / 2) # we only want the first column!
    {
    
    word = (newsArticles[[c(i,j)]]);
    cat("Searching for the word: ", word);
    cat("\n");
    
    cat("Current article: ", i);
    cat("\n");
    
    # set document ID
    newsArticles[[i]][j,2] = i;
    
    ID = newsArticles[[i]][j,2];
    cat("Now the document ID is set to", ID);
    cat("\n\n");
  }
  
}

# order by first column (text)
df[order(df[,1],df[,2],decreasing=FALSE),]
  
df<-matrix(data=c(3,7,5,0,1,0,0,0,0,8,0,9), ncol=2)

df[order(df[,1],df[,2],decreasing=TRUE),]


  