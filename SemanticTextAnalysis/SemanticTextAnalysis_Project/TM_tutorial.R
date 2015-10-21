# trying out tm main tutorial

# get files in texts directory
txt <- system.file("texts", "txt", package = "tm")
(ovid <- VCorpus(DirSource(txt, encoding = "UTF-8"), readerControl = list(language = "lat")))

#(variable) is the same as print(variable)

# read directly from vector sources
docs = c("This is tweet A", "This is tweet B");
VCorpus(VectorSource(docs));

# reuters XML file
#install.packages("XML")
reut21578 <- system.file("texts", "crude", package = "tm")
reuters <- VCorpus(DirSource(reut21578), readerControl = list(reader = readReut21578XMLasPlain))

# export
writeCorpus(ovid);

# little details
print(ovid);

# more details
inspect(ovid[1:2]);

# meta
meta(ovid[[2]]);

# access id
meta(ovid[[2]], "id");

# test if identical
identical(ovid[[2]], ovid[["ovid_2.txt"]])

# get characters
writeLines(as.character(ovid[[2]]))

# lapply applies function X to all elements in the list
lapply(ovid[1:2], as.character)

# TRANSFORMATIONS --------

# remove whitespaces
reuters <- tm_map(reuters, stripWhitespace)

# convert to lower case
reuters <- tm_map(reuters, content_transformer(tolower))

# remove stop words
reuters <- tm_map(reuters, removeWords, stopwords("english"))

# stemming
reuters_stemmed = tm_map(reuters, stemDocument)
writeLines(as.character(reuters_stemmed[[2]]))

# filter documents with ID 237 and specific headline
idx <- meta(reuters, "id") == '237' & meta(reuters, "heading") == 'INDONESIA SEEN AT CROSSROADS OVER ECONOMIC CHANGE'
reuters[idx]

# access/modify meta data
DublinCore(ovid[[1]], "Creator") = "Ano Nymous"

# see meta data
meta(ovid[[1]])

# corpus vs index meta data
meta(ovid[[1]], tag = "test", type = "corpus") <- "test meta"
meta(ovid[[1]], type = "corpus")

meta(ovid[[1]], "foo") <- letters[1:20]
