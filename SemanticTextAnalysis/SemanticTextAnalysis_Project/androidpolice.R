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
pageNumber = 2;
new_results = paste(c("page/", pageNumber), collapse = "")
new_results = paste(c(new_results, "/"), collapse = "")
signatures = system.file("CurlSSL", cainfo = "cacert.pem", package = "RCurl")

while(pageNumber < 120){
  new_results = paste(c("page/", pageNumber), collapse = "")
  new_results = paste(c(new_results, "/"), collapse = "")
  new_results <- str_c("http://www.androidpolice.com/", new_results)
  
  
  results <- getURL(new_results)
  results_tree <- htmlParse(results)
  all_links <- c(all_links, xpathSApply(results_tree, "//div[@class='primary column']//h2/a", xmlGetAttr, "href"))
  
  pageNumber = pageNumber +1;

}

# Check the entries
all_links[1]
length(all_links)

# Download all articles (1000+ articles)
for(i in 1:length(all_links)){
  url = all_links[i];
  tmp <- getURL(url, cainfo = signatures)
  write(tmp, str_c("androidpolice/", i, ".html"))
}

# Check results
length(list.files("androidpolice"))
list.files("androidpolice")[1:3]
head(list.files("androidpolice")) #first or last part of object

### 10.2 Processing Textual Data
### --------------------------------------------------------------

### 10.2.1 Large-scale text operations - the tm package
### --------------------------------------------------------------

# Get press release
tmp <- readLines("androidpolice/1.html")
tmp <- str_c(tmp, collapse = "")
tmp <- htmlParse(tmp)
release = NULL
release <- xpathSApply(tmp, "//div[@class='post-content']//p", xmlValue)
release = str_c(release ,collapse=',')  # make into a single string


### GET META DATA:

## https://stackoverflow.com/questions/22342501/how-to-get-information-within-meta-name-tag-in-html-using-htmlparse-and-xpa

# article section (meta data)
# <meta property="article:section" content="Accessories"/>

# meta data: section
content = tmp["//meta/@content"]

# get all meta data, but mark everything except 'property' as NULL
properties = xpathSApply(tmp, "//meta", xmlGetAttr, "property")


# find the meta data containing the 'section' property
sectionIndex = 0;
dateIndex = 0;
for (j in 1:length(properties))
{
  if (grepl("article:section",properties[j]) == TRUE)
    sectionIndex = j;
  
  if (grepl("article:published_time",properties[j]) == TRUE)
    dateIndex = j;
}

# set the section and publication date
section = content[sectionIndex];
date = content[dateIndex];

# make from list to string character
section = paste(c(section), collapse='')
date = paste(c(date), collapse='')
#date = strsplit(date, "T")[[1]] only take the first string (without clock time)

# Create a corpus from a vector
release_corpus = NULL;
release_corpus <- Corpus(VectorSource(release))
head(release_corpus)

# Setting the meta information
meta(release_corpus[[1]], "section") <- section
meta(release_corpus[[1]], "publication_date") <- date[1]
meta(release_corpus[[1]])

n <- 1
## make corpus of all articles
for(i in 2:length(list.files("androidpolice/")))
#for(i in 1:20)
{
  tmp <- readLines(str_c("androidpolice/", i, ".html"))
  tmp <- str_c(tmp, collapse = "")
  tmp <- htmlParse(tmp)
  
  # article content
  release <- xpathSApply(tmp, "//div[@class='post-content']//p", xmlValue)
  release = str_c(release ,collapse=',')  # make into a single string
  
  # meta data: section
  content = tmp["//meta/@content"]
  
  # get all meta data, but mark everything except 'property' as NULL
  properties = xpathSApply(tmp, "//meta", xmlGetAttr, "property")
  
  # find the meta data containing the 'section' property
  sectionIndex = 0;
  dateIndex = 0;
  for (j in 1:length(properties))
  {
    if (grepl("article:section",properties[j]) == TRUE)
      sectionIndex = j;
    
    if (grepl("article:published_time",properties[j]) == TRUE)
      dateIndex = j;
  }

  # set the section and publication date
  section = content[sectionIndex];
  date = content[dateIndex];
  
  # make from list to string character
  section = paste(c(section), collapse='')
  date = paste(c(date), collapse='')
  date = strsplit(date, "T")[[1]] #only take the first string (without clock time)
  
  #print(section)
  
  if(length(release) != 0)
  {
    n <- n + 1
    tmp_corpus <- Corpus(VectorSource(release))
    release_corpus <- c(release_corpus, tmp_corpus)
    meta(release_corpus[[n]], "section") <- section
    meta(release_corpus[[n]], "publication_date") <- date[1]
  }
}
release_corpus


# Inspect meta data
meta_section <- meta(release_corpus, type = "local", tag = "section")
meta_publication_date <- meta(release_corpus, type = "local", tag = "publication_date")

meta_data <- data.frame(
  section = unlist(meta_section), 
  publication_date = unlist(meta_publication_date)
)
table(as.character(meta_data[, "section"]))
table(as.character(meta_data[, "publication_date"]))

head(meta_data["section"])
head(meta_data["publication_date"])

######



# Filtering the corpus (only those with enought sources; for supervised learning)
release_corpus <- release_corpus[
  meta(release_corpus, tag = "section") == "Accessories" | #22
    meta(release_corpus, tag = "section") == "Android OS" | #10
    #meta(release_corpus, tag = "section") == "Android TV" | #6
    meta(release_corpus, tag = "section") == " Android Wear " | #11
    #meta(release_corpus, tag = "section") == "APK Teardown " | #6
    meta(release_corpus, tag = "section") == "Applications" | #145
    meta(release_corpus, tag = "section") == "AT&T" | #11
    #meta(release_corpus, tag = "section") == "Chromecast" | #5
    meta(release_corpus, tag = "section") == "Deals" | #37
    #meta(release_corpus, tag = "section") == "Development" | #6
    meta(release_corpus, tag = "section") == "Device Updates" | #10
    meta(release_corpus, tag = "section") == "Games" | #27
    meta(release_corpus, tag = "section") == "Google" | #29
    #meta(release_corpus, tag = "section") == "Leaks" | #6
    meta(release_corpus, tag = "section") == "Marshmallow 6.0" | #20
    meta(release_corpus, tag = "section") == "News" #54
  ]
release_corpus
tm_filter(release_corpus, FUN = function(x) any(grep("Google", content(x))))

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
findAssocs(tdm, "Google", .7)

### 10.3 Supervised Learning Techniques
### --------------------------------------------------------------

### 10.3.5 Application: Governemnt press releases
### --------------------------------------------------------------

dtm <- DocumentTermMatrix(release_corpus)
dtm <- removeSparseTerms(dtm, 1-(10/length(release_corpus)))
dtm

# Labels
org_labels <- unlist(meta(release_corpus, "section"))
org_labels[1:3]

training_size = (length(release_corpus) / 100) * 80;
test_size = training_size + 1;#(length(release_corpus) / 100) * 20;
# Create container
N <- length(org_labels)
container <- create_container(
  dtm,
  labels = org_labels,
  trainSize = 1:training_size,
  testSize = test_size:N,
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
  correct_label = org_labels[test_size:N],
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
      release_corpus, tag = "section"
    ) == "Accessories"
  )[1:20],
  which(
    meta(
      release_corpus, tag = "section"
    ) == "Applications"
  )[1:20],
  which(
    meta(
      release_corpus, tag = "section"
    ) == "Marshmallow 6.0"
  )[1:20],
  which(
    meta(
      release_corpus, tag = "section"
    ) == "AT&T"
  )[1:18] # <--- this number CANNOT be lower than the actual occurences of articles (otherwise NA error)
)]

table(unlist(meta(short_corpus, "section")))


# Create shortened Document-Term-Matrix
short_dtm <- DocumentTermMatrix(short_corpus)
short_dtm <- removeSparseTerms(short_dtm, 1-(5/length(short_corpus)))
rownames(short_dtm) <- c(rep("Accessories", 20), rep("Applications", 20), rep("Marshmallow", 20), rep("AT&T", 18))

# Create dendrogram
dist_dtm <- dist(short_dtm)
out <- hclust(dist_dtm, method = "ward.D")
plot(out)

# draw the clusters: http://www.instantr.com/2013/02/12/performing-a-cluster-analysis-in-r/
rect.hclust(out, 5)

# Unsupervised classification
# use length of org_labels
# length(table(org_labels))
lda_out <- LDA(dtm, 10)
posterior_lda <- posterior(lda_out)
lda_topics <- data.frame(t(posterior_lda$topics))
## Setting up matrix for mean probabilities
mean_topic_matrix <- matrix(
  NA,
  nrow = 10,
  ncol = 10,
  dimnames = list(
    names(table(org_labels)),
    str_c("Topic_", 1:10)
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
ctm_out <- CTM(dtm, 11)
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