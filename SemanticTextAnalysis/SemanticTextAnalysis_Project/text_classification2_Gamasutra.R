### --------------------------------------------------------------
### AUTOMATED DATA COLLECTION WITH R
### SIMON MUNZERT, CHRISTIAN RUBBA, PETER MEISSNER, DOMINIC NYHUIS
###
### CODE CHAPTER 10: STATISTICAL TEXT PROCESSING
### --------------------------------------------------------------

setwd("C:/Users/Wikzo/Documents/Hagenberg_2015/SemanticTextAnalysis/SemanticTextAnalysis_Project")
getwd()

# load packages
library(RCurl)
library(XML)
library(stringr)
library(tm)
library(SnowballC)
library(RWeka)
library(RTextTools)
library(topicmodels)

### 10.1 The running example: Classifying press releases of the British government
### --------------------------------------------------------------
### INITIAL STUFF - only do this one time! -----------------------

# Downloading all results

all_links <- character()
pageNumber = 1;
new_results = paste(c("?page=", pageNumber), collapse = "")
signatures = system.file("CurlSSL", cainfo = "cacert.pem", package = "RCurl")


while(pageNumber < 3){
  new_results = paste(c("?page=", pageNumber), collapse = "")
  new_results <- str_c("http://gamasutra.com/pressreleases_index.php", new_results)

  
  results <- getURL(new_results, cainfo = signatures)
  results_tree <- htmlParse(results)
  all_links <- c(all_links, xpathSApply(results_tree, "//td[@class='NewsContent']/span[@class='story_title']/a", xmlGetAttr, "href"))
  
  pageNumber = pageNumber +1;
  
  #new_results <- xpathSApply(results_tree, "//nav[@id='show-more-documents']//[@class='pageNav']//a", xmlGetAttr, "href")
}

# Check the entries
all_links[1]
length(all_links)

# Download all press releases (40 articles)
for(i in 1:length(all_links)){
  #url <- str_c("http://gamasutra.com/", all_links[i])
  url = all_links[i];
  tmp <- getURL(url, cainfo = signatures)
  write(tmp, str_c("Gamasutra/", i, ".html"))
}

# Check results
length(list.files("Gamasutra"))
list.files("Gamasutra")[1:3]
head(list.files("Gamasutra")) #first or last part of object

### 10.2 Processing Textual Data
### --------------------------------------------------------------

### 10.2.1 Large-scale text operations - the tm package
### --------------------------------------------------------------

# Get press release
tmp <- readLines("Gamasutra/1.html")
tmp <- str_c(tmp, collapse = "")
tmp <- htmlParse(tmp)
release = NULL
release <- xpathSApply(tmp, "//td[@class='newsText']/p", xmlValue)
release = str_c(release ,collapse=',')  # make into a single string

# Get meta information (organisation and date of publication)
## https://stackoverflow.com/questions/22342501/how-to-get-information-within-meta-name-tag-in-html-using-htmlparse-and-xpa

# keywords
keywords = tmp["//meta/@name"]
content = tmp["//meta/@content"]
#cbind(keywords,content)
keys = content[3]
keys = str_c(keys ,collapse=',')  # make into a single string NOT SURE IF THIS WORKS?

# publication date
date <- xpathSApply(tmp, "//td[@class='newsDate']", xmlValue)

# Create a corpus from a vector
release_corpus = NULL;
release_corpus <- Corpus(VectorSource(release))
head(release_corpus)

# Setting the meta information
meta(release_corpus[[1]], "keywords") <- "keys"
meta(release_corpus[[1]], "publication_date") <- "date"
meta(release_corpus[[1]])

n <- 1
## make corpus of all articles
for(i in 2:length(list.files("Gamasutra/"))){
  tmp <- readLines(str_c("Gamasutra/", i, ".html"))
  tmp <- str_c(tmp, collapse = "")
  tmp <- htmlParse(tmp)
  
  # article content
  release <- xpathSApply(tmp, "//td[@class='newsText']/p", xmlValue)
  release = str_c(release ,collapse=',')  # make into a single string 
  
  # meta data: keywords
  content = tmp["//meta/@content"]
  keys = content[3]
  keys = str_c(keys ,collapse=',')  # make into a single string NOT SURE IF THIS WORKS?
  
  # meta data: publication date
  date <- xpathSApply(tmp, "//td[@class='newsDate']", xmlValue)

  
  if(length(release) != 0){
    n <- n + 1
    tmp_corpus <- Corpus(VectorSource(release))
    release_corpus <- c(release_corpus, tmp_corpus)
    meta(release_corpus[[2]], "keywords") <- keys
    meta(release_corpus[[n]], "publication_date") <- date
  }
}
release_corpus

# Inspect meta data
meta_keywords <- meta(release_corpus, type = "local", tag = "keywords")
meta_publication_date <- meta(release_corpus, type = "local", tag = "publication_date")

meta_data <- data.frame(
  keywords = unlist(meta_keywords), 
  publication_date = unlist(meta_publication_date)
)
table(as.character(meta_data[, "keywords"]))
table(as.character(meta_data[, "publication_date"]))

head(meta_data["keywords"])
head(meta_data["publication_date"])

######

# NOT SURE TO USE THIS?
# Filtering the corpus (only those with enought sources; for supervised learning)
release_corpus <- release_corpus[
  meta(release_corpus, tag = "organisation") == "Department for Business, Innovation & Skills" |
    meta(release_corpus, tag = "organisation") == "Department for Communities and Local Government" |
    meta(release_corpus, tag = "organisation") == "Department for Environment, Food & Rural Affairs" |
    meta(release_corpus, tag = "organisation") == "Foreign & Commonwealth Office" |
    meta(release_corpus, tag = "organisation") == "Ministry of Defence" |
    meta(release_corpus, tag = "organisation") == "Wales Office"        
  ]
release_corpus
tm_filter(release_corpus, FUN = function(x) any(grep("Nintendo", content(x))))

### 10.2.2 Building a term-document matrix
### --------------------------------------------------------------

# sparse: matrix contains zero's
tdm <- TermDocumentMatrix(release_corpus)
tdm

### 10.2.3 Data cleansing
### --------------------------------------------------------------

# Remove numbers
release_corpus <- tm_map(release_corpus, removeNumbers)

# Remove punctuation
release_corpus <- tm_map(
  release_corpus, 
  content_transformer(
    function(x, pattern){
      gsub(
        pattern = "[[:punct:]]", 
        replacement = " ",
        x
      )
    }
  )
)

# Remove stopwords
length(stopwords("en"))
stopwords("en")[1:10]
release_corpus <- tm_map(release_corpus, removeWords, words = stopwords("en"))

# Convert to lower case
release_corpus <- tm_map(release_corpus, content_transformer(tolower))

# Stem documents
release_corpus <- tm_map(release_corpus, stemDocument)

### 10.2.4 Sparsity and n-grams
### --------------------------------------------------------------

tdm <- TermDocumentMatrix(release_corpus)
tdm

# Remove sparse terms
tdm <- removeSparseTerms(tdm, 1-(10/length(release_corpus)))
tdm

# Bigrams (not needed at the moment...)
BigramTokenizer <- function(x){
  NGramTokenizer(x, Weka_control(min = 2, max = 2))
}
tdm_bigram <- TermDocumentMatrix(release_corpus, control = list(tokenize = BigramTokenizer))
tdm_bigram

# Find associations
findAssocs(tdm, "nintendo", .7)

### 10.3 Supervised Learning Techniques
### --------------------------------------------------------------

### 10.3.5 Application: Governemnt press releases
### --------------------------------------------------------------

dtm <- DocumentTermMatrix(release_corpus)
dtm <- removeSparseTerms(dtm, 1-(10/length(release_corpus)))
dtm

# Labels
org_labels <- unlist(meta(release_corpus, "keywords"))
org_labels[1:3]

# Create container
N <- length(org_labels)
container <- create_container(
  dtm,
  labels = org_labels,
  trainSize = 1:30,
  testSize = 31:N,
  virgin = F
)
slotNames(container)

# Train models
svm_model <- train_model(container, "SVM")
tree_model <- train_model(container, "TREE")
maxent_model <- train_model(container, "MAXENT")

# Classify models
svm_out <- classify_model(container, svm_model)
tree_out <- classify_model(container, tree_model)
maxent_out <- classify_model(container, maxent_model)

head(svm_out)
head(tree_out)
head(maxent_out)

# Construct data frame with correct labels
labels_out <- data.frame(
  correct_label = org_labels[401:N],
  svm = as.character(svm_out[,1]),
  tree = as.character(tree_out[,1]),
  maxent = as.character(maxent_out[,1]),
  stringsAsFactors = F
)

## SVM performance
table(labels_out[,1] == labels_out[,2])
## Random forest performance
table(labels_out[,1] == labels_out[,3])
## Maximum entropy performance
table(labels_out[,1] == labels_out[,4])

prop.table(table(labels_out[,1] == labels_out[,4]))

### 10.4 Unsupervised Learning Techniques
### --------------------------------------------------------------

### 10.4.2 Application: Government press releases
### --------------------------------------------------------------

# Hierarchical clustering
# Create shortened corpus
short_corpus <- release_corpus[c(
  which(
    meta(
      release_corpus, tag = "organisation"
    ) == "Department for Business, Innovation & Skills"
  )[1:20],
  which(
    meta(
      release_corpus, tag = "organisation"
    ) == "Wales Office"
  )[1:20],
  which(
    meta(
      release_corpus, tag = "organisation"
    ) == "Department for Environment, Food & Rural Affairs"
  )[1:20]
)]

table(unlist(meta(short_corpus, "organisation")))


# Create shortened Document-Term-Matrix
short_dtm <- DocumentTermMatrix(short_corpus)
short_dtm <- removeSparseTerms(short_dtm, 1-(5/length(short_corpus)))
rownames(short_dtm) <- c(rep("Defence", 20), rep("Wales", 20), rep("Environment", 20))

# Create dendrogram
dist_dtm <- dist(short_dtm)
out <- hclust(dist_dtm, method = "ward.D")
plot(out)

# Unsupervised classification
lda_out <- LDA(dtm, 6)
posterior_lda <- posterior(lda_out)
lda_topics <- data.frame(t(posterior_lda$topics))
## Setting up matrix for mean probabilities
mean_topic_matrix <- matrix(
  NA,
  nrow = 6,
  ncol = 6,
  dimnames = list(
    names(table(org_labels)),
    str_c("Topic_", 1:6)
  )
)
## Filling matrix
for(i in 1:6){
  mean_topic_matrix[i,] <- apply(lda_topics[, which(org_labels == rownames(mean_topic_matrix)[i])], 1, mean)
}
## Outputting rounded matrix
round(mean_topic_matrix, 2)

# Inspecting associated terms
terms(lda_out, 10)

# Correlated topic model
ctm_out <- CTM(dtm, 6)
terms(ctm_out, 10)

# Plotting the output
posterior_ctm <- posterior(ctm_out)
ctm_topics <- data.frame(t(posterior_ctm$topics))

par(mfrow = c(2,3), cex.main = .8, pty = "s", mar = c(5, 5, 1, 1))
for(topic in 1:2){
  for(orga in names(table(org_labels))){
    tmp.data <- ctm_topics[topic, org_labels == orga]
    plot(
      1:ncol(tmp.data),
      sort(as.numeric(tmp.data)),
      type = "l",
      ylim = c(0, 1),
      xlab = "Press releases",
      ylab = str_c("Posterior probability, topic ", topic),
      main = str_replace(orga, "Department for", "")
    )
  }
}