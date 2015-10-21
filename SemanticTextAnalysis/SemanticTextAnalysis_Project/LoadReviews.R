# from: https://deltadna.com/blog/text-mining-in-r-for-term-frequency/

install.packages('tm');

library(tm);

#loading the data
reviews = read.csv("reviews.csv", stringsAsFactors = FALSE);

#View(reviews) #view the data in a table

# combine all review text together
review_text = paste(reviews$text, collapse="");

# setting up source and corpus
review_source = VectorSource(review_text);
corpus = Corpus(review_source);

# cleaning
corpus = tm_map(corpus, content_transformer(tolower));
corpus = tm_map(corpus, removePunctuation);
corpus = tm_map(corpus, stripWhitespace);
corpus = tm_map(corpus, removeWords, stopwords("english"));

# making document-term matrix
dtm = DocumentTermMatrix(corpus);
dtm2 = as.matrix(dtm);

# finding the most frequent terms
frequency = colSums(dtm2)
frequency = sort(frequency, decreasing=TRUE);
# str(frequency)
head(frequency) # 6 most common words

install.packages('wordcloud');
library(wordcloud);

words = names(frequency);
wordcloud(words[1:100], frequency[1:100]);
