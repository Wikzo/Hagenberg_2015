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

# make counter list where all data is set to default zero
countList =list(rep(list(c(0)), times=length(allWords)));
counter = 0;

# look through all words and set document ID
for (i in 1:length(allWords)) # loop through all words
{
  for (j in 1:length(newsArticles)) # loop through all articles
  {
    if (is.element(allWords[[i]], newsArticles[[j]])) # check if word matches article
    {
      indexList[[1]][[i]] = union(indexList[[1]][[i]],j); # set document ID to list
      counter = countList[[1]][[i]];
      counter = counter + 1;
      countList[[1]][[i]] = counter;
    }

  }
}
# set the second element to all the words
indexList[[2]] =  allWords;
indexList[[3]] = countList[[1]];


# NOTE: list too long to be shown in Environment list
indexList[[1]] # see all document IDs
indexList[[2]] # see all words

# find the dictionary entry that contains word "of"
position = match("of", indexList[[2]]) # position of word in total words

# retrive the document IDs in that position: document 1+2+3+4+5
indexList[[c(1,position)]]

# simple test ----------------
# searches for word number 371: "respectively
indexList[[c(2,371)]]

# how many times does the word "respectively" occur? 1 time
length(indexList[[c(2,371)]])

# what document(s) includes the word "respectively"? document 2
indexList[[c(1,371)]]

# simple test ---------------- end

# query optimization (AND) ----------
# we want to look for the words "of" AND "Friday"

# let's look throught the lists with fewer elements first


# query optimization (AND) ---------- end