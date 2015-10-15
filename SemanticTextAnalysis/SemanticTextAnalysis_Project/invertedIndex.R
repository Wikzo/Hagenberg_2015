news1 = scan("news1.txt", what="character")
news2 = scan("news2.txt", what="character")
news3 = scan("news3.txt", what="character")
news4 = scan("news4.txt", what="character")
news5 = scan("news5.txt", what="character")

newsArticles = list(news1, news2, news3, news4, news5); # list of all articles

#list of all words
allWords = union(news1, news2);
allWords = union(allArticles, news3);
allWords = union(allArticles, news4);
allWords = union(allArticles, news5);


# make index list with 727 entries and 1 column set to NULL
indexList = list(rep(list(c()), times=length(allWords)));

# look through all words and set document ID
for (i in 1:length(allWords)) # loop through all words
{
  for (j in 1:length(newsArticles)) # loop through all articles
  {
    
    if (is.element(allWords[[i]], newsArticles[[j]])) # check if word matches article
    {
      indexList[[1]][[i]] = union(indexList[[1]][[i]],j); # set document ID to list
    }

  }
}
# set the second element to all the words
indexList[[2]]<- allWords;


# NOTE: list too long to be shown in Environment list
indexList[[1]] # see all document IDs
indexList[[2]] # see all words
