## --- Created by Gustav Dahl, IM 2015 (October) --- ###

# remove all variables
rm(list=ls(all=TRUE))

# load in the files
news1 = scan("news1.txt", what="character")
news2 = scan("news2.txt", what="character")
news3 = scan("news3.txt", what="character")
news4 = scan("news4.txt", what="character")
news5 = scan("news5.txt", what="character")

# put all articles in a list
newsArticles = list(news1, news2, news3, news4, news5); # list of all articles

#list of all unique
allWords = union(news1, news2);
allWords = union(allWords, news3);
allWords = union(allWords, news4);
allWords = union(allWords, news5);

# make index list with 727 entries and 1 column set to NULL
indexList = list(rep(list(c()), times=length(allWords)));

# make counter list where all data is set to default zero
countList =list(rep(list(c(0)), times=length(allWords)));

# look through all words and set document ID
for (i in 1:length(allWords)) # loop through all words
{
  for (j in 1:length(newsArticles)) # loop through all articles
  {
    if (is.element(allWords[[i]], newsArticles[[j]])) # check if word matches article
    {
      indexList[[1]][[i]] = union(indexList[[1]][[i]],j); # set document ID to list
      countList[[1]][[i]] =  countList[[1]][[i]] + 1; # increase frequency for this word by 1
    }
  }
}

indexList[[2]] =  allWords; # set the second element to all the words
indexList[[3]] = countList[[1]]; # set the third element to the frequency of the words

# rename the columns
names(indexList)[1] = "ID";
names(indexList)[2] = "Word";
names(indexList)[3] = "Freq";


# NOTE: list is too long to be shown in Environment list
indexList[[1]] # see all document IDs
indexList[[2]] # see all words

# find the dictionary entry that contains word "news"
position = match("news", indexList[[2]]) # position of word in total words

# TASK 1) Write a query for your index that returns all documents comntaining the search term
indexList[[c(1,position)]] # what document(s) includes the word "news"? Document 1 + 3 + 5

# searches for word "news"
indexList[[c(2,position)]]

# how many times does the word "news" occur? 3 times
indexList[[c(3,position)]]


# TASK 2) extend your query for AND a OR -----------
positionA = match("million", indexList[[2]]) # position of word in total words
positionB = match("Reuters", indexList[[2]]) # position of word in total words


A = indexList[[c(1,positionA)]] # document IDs with word "million"
B = indexList[[c(1,positionB)]] # document IDs with word "Reuters"

# OR comparsion
AB_OR = union(A,B);  # words "million" OR "Reuters" are found in article 1 + 3 + 5

# AND comparison
AB_AND = intersect(A,B); # words "million" AND "Reuters" are found in article 5


# TASK 3) extending index to "positindonal" Index -----

positionList = list(rep(list(c()), times=length(allWords)));

# look through all words and set document ID
for (i in 1:length(allWords)) # loop through all words
{
  for (j in 1:length(newsArticles)) # loop through all articles
  {
    if (is.element(allWords[[i]], newsArticles[[j]])) # check if word matches article
    {
      tempPosition <- c();
      
      for (k in 1:length(newsArticles[[j]]))
      {
        if (is.element(allWords[[i]], newsArticles[[j]][[k]]))
        {
          tempPosition = c(tempPosition, k);
        }
      }
      # Store document ID j in extra column
      positionList[[1]][[i]] = c(positionList[[1]][[i]],j,list(tempPosition));
    }
    
  }
}

totalList = union(indexList,positionList); # make total list with the new positional index

position = match("Taliban", indexList[[2]]) # look for word "Taliban"

# at what positions are the word "Taliban" stored? Document ID 1 at position 3 + 52 + 107 + 124
totalList[[c(4,position)]]