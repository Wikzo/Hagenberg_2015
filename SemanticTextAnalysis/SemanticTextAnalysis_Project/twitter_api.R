library(streamR)
library(lubridate)
library(stringr)
library(plyr)
library(twitteR)
library(RCurl)
library(ROAuth)

# APP auf https://apps.twitter.com anmelden und tokens holen

requestURL <-  "https://api.twitter.com/oauth/request_token"
accessURL =    "https://api.twitter.com/oauth/access_token"
authURL =      "https://api.twitter.com/oauth/authorize"
consumerKey =   "g8V1ywv6SEJphCGseyWagmeoy"
consumerSecret = "7zX8CHnstU9gUBFC3qRVClsKG9x8MqdQbuReMb3q1AR4VeAkS1"

twitCred <- OAuthFactory$new(consumerKey=consumerKey,
                             consumerSecret=consumerSecret,
                             requestURL=requestURL,
                             accessURL=accessURL,
                             authURL=authURL)
twitCred$handshake(cainfo = system.file("CurlSSL", "cacert.pem", package = "RCurl"))

# Filter the Twitter stream
filterStream("tweets.json", track = c("iwatch", "iphone"),
             timeout = 300, oauth = twitCred)

# Parse tweets
tweets <- parseTweets("tweets.json", simplify = TRUE)

----------------------------------------------------------
  
  # Set locale of machine
  Sys.setlocale("LC_TIME", "English")

# Get info on first tweet
tweets$created_at[1]

# Parse values
tweets$time <- as.POSIXct(tweets$created_at, tz = "UTC", format = "%a %b %d %H:%M:%S %z %Y")

# Round to nearest hour
tweets$round_minute <- round_date(tweets$time, unit = "minute")


myMinutes = strftime(tweets$time, format="%M")

length(myMinutes)



startTime = myMinutes[1] #48
endTime = myMinutes[2866] #57

currentMinute = 0;
indexList = list(0,0,0,0,0,0,0,0,0,0,0);
index = 0;
for (i in 1:length(tweets$time))
{
  if (myMinutes[i] != currentMinute)
  {
    index = index + 1;
    currentMinute = myMinutes[i];
  }
  
  indexList[index] = indexList[[index]] + 1;
}


plot(c(1:11),unlist(indexList),type="o", col="blue")
axis(side=1, at=c(0:11))
