package com.yunjae.spark

import java.io.File

import org.scalatest.Matchers._
import com.yunjae.spark.task.FindTopPhrasesJob
import org.apache.commons.io.FileUtils


class FindTopPhrasesJobTest extends SparkSuite {
  override def appName: String = "scala-big-data-spark"

  test("should find top 2 phrase in given text input delimited by") {
    //given
    val inputTest = spark.makeRDD(List("this;is;a;text;and;this;should;be;find;top;phrases;in;this;text"))

    //when
    val res = FindTopPhrasesJob.findTopPhrases(inputTest, 2)

    //then
    res should contain theSameElementsAs List (
      ("this", 3),
      ("text", 2)
    )
  }


  test("should find top 2 phrases in given text input delimited by characters other than a-z, A-Z, 0-9") {
    //given
    val inputText = spark.makeRDD(List("this;is+a?TeXt!and this:should)be(find&top^phrases_in\nTHIS*TexT"))

    //when
    val res = FindTopPhrasesJob.findTopPhrases(inputText, 2)

    //then
    res should contain theSameElementsAs List(
      ("this", 3),
      ("text", 2)
    )
  }

  test("should find top 10 phrases in complex text input") {
    //given
    val inputText = spark.makeRDD(List(FileUtils.readFileToString(new File("book_to_analyze.txt"))))

    //when
    val res = FindTopPhrasesJob.findTopPhrases(inputText, 10)

    //then
    res should contain theSameElementsAs List(
      ("the", 343),
      ("of", 177),
      ("and", 132),
      ("in", 131),
      ("to", 104),
      ("a", 94),
      ("gutenberg", 93),
      ("or", 89),
      ("project", 87),
      ("you", 73))
  }

}
