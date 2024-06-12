package com.example.dicegamecwk

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    //com refers to computer

    //below values are set because these will be the values affected when orientation changes

    companion object {
        var comWins = 0// number of time the computer won

        var playerWins = 0 // the number of times player won
    }

    var gameStarted=false
    var indexSumOfDice=4
    var throwPressed = false
    var scorePressed = false

    var scoreCom = 0// score of the computer
    var scorePlayer = 0//score of the player
    var reRollCount = 0//number of throws per round for the player
    var roundCount = 1// game starts with round 1
    var computerReRollCount = 0//re roll count a computer attempts per round in random strategy
    var winningScore = 101//default winning score. it will update if user sets score later
    var gameWon = false //boolean to check if game is won

    //array to hold if dice is clicked
     val chosenDiceArray = arrayListOf<Boolean>(false, false, false, false, false)

    //represent the value of each face of payer die currently
    var dieValuesPlayer = intArrayOf(0, 0, 0, 0, 0)
    var dieValueComputer = intArrayOf(0, 0, 0, 0, 0)
    var imgArrayPlayer = listOf<ImageView>()//the list that will hold the image views of player
    var imgArrayComputer = listOf<ImageView>()//the list that will hold the image views of computer
    val dicePicArray = intArrayOf(
        R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4,
        R.drawable.dice_5, R.drawable.dice_6
    )//the die images from 1 to 6



    var hardMode = false //if hard mode is set this happens


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()//to hide bar
        setContentView(R.layout.game_activity)
        val txtTotalWins = findViewById<TextView>(R.id.totalScores)
        //shows the values of of the total wins per person
        txtTotalWins.text = buildString {
            append("H: ")
            append(playerWins)
            append("/C:")
            append(comWins)
        }
        //below code gets the values from the new activity of the player name
        val playerName = intent.getStringExtra("playerName")
        val txtPlayerName = findViewById<TextView>(R.id.txtPlayerName)
        txtPlayerName.text = playerName.toString()//sets the name


        val txtScore = findViewById<TextView>(R.id.score)//the top right score count



        //image view of computer showing the picture of a robot avatar
        val imgComputer = findViewById<ImageView>(R.id.imgV_Computer)
        val imgPlayer = findViewById<ImageView>(R.id.imgV_Player)

        imgComputer.setImageResource(R.drawable.robot)
        imgPlayer.setImageResource(R.drawable.player)

        // image view of player showing picture of player avatar
        val playerTargetPoint = findViewById<EditText>(R.id.CustomPoints)
        //txt that tells user to change the score. set so it will not be visible later
        val txtEditValue = findViewById<TextView>(R.id.txtEditPoints)

        val txtMainMiddle = findViewById<TextView>(R.id.txtMiddle)//shows the main text in the middle
        txtMainMiddle.text =
            getString(R.string.changeTargetScore)//shows the users to change target score
        val txtTopMiddle = findViewById<TextView>(R.id.roundTop)
        txtTopMiddle.text =
            "Round $roundCount \n Target $winningScore"//shows the round and target score

        updateMidText(txtTopMiddle,txtMainMiddle)
        //image view of computer die
        val imgVCom1 = findViewById<ImageView>(R.id.imgVComDie1)
        val imgVCom2 = findViewById<ImageView>(R.id.imgVComDie2)
        val imgVCom3 = findViewById<ImageView>(R.id.imgVComDie3)
        val imgVCom4 = findViewById<ImageView>(R.id.imgVComDie4)
        val imgVCom5 = findViewById<ImageView>(R.id.imgVComDie5)


        //image view of player die
        val imgVPly1 = findViewById<ImageView>(R.id.imgVPlyDie1)
        val imgVPly2 = findViewById<ImageView>(R.id.imgVPlyDie2)
        val imgVPly3 = findViewById<ImageView>(R.id.imgVPlyDie3)
        val imgVPly4 = findViewById<ImageView>(R.id.imgVPlyDie4)
        val imgVPly5 = findViewById<ImageView>(R.id.imgVPlyDie5)
        val btnSetScore = findViewById<Button>(R.id.btnSetScore)

        //setting the above in an array for ease of use
        imgArrayComputer = listOf<ImageView>(imgVCom1, imgVCom2, imgVCom3, imgVCom4, imgVCom5)
        imgArrayPlayer = listOf<ImageView>(imgVPly1, imgVPly2, imgVPly3, imgVPly4, imgVPly5)


        for (i in 0..indexSumOfDice) {
            imgArrayPlayer[i].setOnClickListener {
                highlightDie(imgArrayPlayer[i], chosenDiceArray[i])
                // clickDie1 = !clickDie1
                chosenDiceArray[i] = !chosenDiceArray[i]

            }
        }


        val btnThrow = findViewById<Button>(R.id.btn_Throw)//the throw button

        val btnScore = findViewById<Button>(R.id.btn_Score)//the btn score


        val switchBtnMode = findViewById<Switch>(R.id.switch1)//switch button
        object : CountDownTimer(30, 30) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                txtScore.text = "Computer $scoreCom \nPlayer $scorePlayer "
                if (gameStarted){
                    updateMidText(txtTopMiddle,txtMainMiddle)
                    playerTargetPoint.isVisible=false
                    txtEditValue.isVisible=false
                    switchBtnMode.isVisible=false
                    btnSetScore.isVisible=false
                }

                if (gameWon){
                    btnThrow.isClickable=false
                    txtEditValue.isVisible=false
                    btnScore.isClickable=false
                    switchBtnMode.isVisible=false
                    btnSetScore.isVisible=false
                    playerTargetPoint.isVisible=false
                    txtMainMiddle.text = "Game over,Please press back \nto play the game again"
                }

            }
        }.start()



        switchBtnMode.setOnCheckedChangeListener { _, isChecked ->
            //check if the button is clicked or not

            //if selection is true
            hardMode = if (isChecked) {
                //show toast
                Toast.makeText(this, "Hard mode", Toast.LENGTH_SHORT).show()
                //change image to give a better feel
                imgComputer.setImageResource(R.drawable.stratmode)
                true

            } else {
                //sets to original state
                Toast.makeText(this, "Random mode", Toast.LENGTH_SHORT).show()
                imgComputer.setImageResource(R.drawable.robot)
                false

            }

        }

        btnSetScore.setOnClickListener {


            val pointsSet = playerTargetPoint.text.toString().toInt()
            //sets a range of value allowed ot be edited
            if (pointsSet < 50 || pointsSet > 300) {
                Toast.makeText(this, "not in range", Toast.LENGTH_SHORT).show()

            } else {
                //if the integer entered is valid sets points score to winning score
                winningScore = pointsSet
                //disable and hides the below buttons so the user cant change score anymore
                btnThrow.isClickable = true
                btnSetScore.isVisible = false
                playerTargetPoint.isVisible = false
                //show a simple toast to inform user of the target to reach
                Toast.makeText(this, "Target is $winningScore", Toast.LENGTH_SHORT).show()

            }
            updateMidText(txtTopMiddle,txtMainMiddle)


        }



        btnThrow.setOnClickListener {
            diceSound()//plays die sound
            for (i in 0..indexSumOfDice){
                imgArrayPlayer[i].isClickable=true
            }

            gameStarted=true
            btnScore.isClickable = true
            //on throw sets the above to be clickable in case the user try to press score twice
            if (scorePressed) {
                //if the score button is pressed sets rolls count to zero and sets value to false again
                reRollCount = 0
                scorePressed = false

            }
            throwPressed = true
            txtMainMiddle.text = ""

            switchBtnMode.isVisible = false//hides the switch

            //below code is set to run if a tie case occurs

            //check if any value is larger than winning score and is it equal to the other score
            if (scoreCom >= winningScore && scoreCom == scorePlayer) {
                reRollCount=2
                generateRandomDice()//generate random player dice
                generateRandomDiceCom()//generate random computer dice
                score(txtScore, btnThrow, txtMainMiddle, txtTopMiddle, btnScore)//score the above
                updateMidText(txtTopMiddle,txtMainMiddle)
                reRollCount=0
            } else {//if not tie case proceed as normal
                if (btnSetScore.isVisible) {
                    //if set score button has not been used,uses default score
                    //show toast showing target score
                    Toast.makeText(this, "Default target 101", Toast.LENGTH_SHORT).show()
                    btnSetScore.isVisible = false//sets buttons to be invisible so user cant change them
                    txtEditValue.isVisible = false
                    playerTargetPoint.isVisible = false
                }//done in case user does not press score


                if (reRollCount < 3) {//allows up to 2 re rolls
                    generateRandomDice()
                    if (hardMode && reRollCount == 0) {
                        generateComputerStrategy()

                    }else if (!hardMode && reRollCount==0){
                        generateRandomDiceCom()
                    }


                    if (reRollCount == 2) {
                        //if the user has done 2 re rolls counter gets set to zero
                        reRollCount = 0
                        btnScore.performClick()//performs a simulated button click
                        //score is updated


                        Toast.makeText(this, "2 re rolls,Score updated!", Toast.LENGTH_SHORT).show()
                    }


                    reRollCount++//increases the re roll count


                }


                for (i in imgArrayPlayer)
                    //for each image removes the green background
                    unHighlightDice(i)

            }
            updateMidText(txtTopMiddle,txtMainMiddle)


        }






        btnScore.setOnClickListener {
            //below step is done only if hard mode is disabled

            if (!hardMode && computerReRollCount==0 && gameStarted) {

                    //on pressing the score
                    val gen2: Random = Random
                //choosing number of re rolls
                    val noOfReRollsComputer = gen2.nextInt(3)
                    txtMainMiddle.text = "computer is doing $noOfReRollsComputer rolls"
                    if (noOfReRollsComputer != 0) {
                        for (i in 0..noOfReRollsComputer)
                            reRollComputerDie()
                    }
                    computerReRollCount++//increases computer re roll count


            }
            score(txtScore, btnThrow, txtMainMiddle, txtTopMiddle, btnScore)


           // if (hardMode)
          //      score(txtScore, btnThrow, switchBtnMode, txtTopMiddle, btnScore)

            if (!hardMode) {//only considered when not in hard mode
                throwPressed = false //done so the throw button is set as not clicked
            }


        }


    }



    private fun diceSound() {

        val mediaPlayer = MediaPlayer.create(this, R.raw.dies)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            (MediaPlayer.OnCompletionListener {
                mediaPlayer.release()
            })
        }
        //below code is to play sound by invoking media player
    }


    @SuppressLint("SetTextI18n")
    private fun updateMidText(txtTopMiddle: TextView, txtMainMiddle: TextView) {
        //below code is called when use wants to show update the text in the middle
        txtTopMiddle.text = "Round $roundCount \nTarget $winningScore"
        if (!throwPressed) {
            txtMainMiddle.text = "Round $roundCount throw!"


        } else if(reRollCount>0) {
            val recount = 3 - reRollCount
            txtMainMiddle.text = "You have $recount re rolls left"
        }

        if (gameWon)
            txtMainMiddle.text = "Game over,Please press back \nto play the game again"

    }

    @SuppressLint("SetTextI18n")
    private fun winPopup(textView: TextView, playerWon: Boolean, btnScore: Button,
        btnThrow: Button
    ) {

        gameWon=true
        val popView = layoutInflater.inflate(R.layout.winning_popup, null)
        btnScore.isClickable = false//disables buttons so user cant click them anymore
        btnThrow.isClickable = false
        //textView.setText(" ")
        textView.text = "Game over,Please press back \nto play the game again"
        for (i in imgArrayPlayer)
           i.isClickable=false//sets it so that images cant be clicked after winning

        R.layout.winning_popup
        //popWindow.contentView=popView1
        //val width=LinearLayout.LayoutParams.WRAP_CONTENT
        val width = 900//sets width and height of pop up windows
        val height = 1000
        val focusable = true//allows to interact with the inside
        val popUpWinWindow = PopupWindow(popView, width, height, focusable)//sets the above properties
        val winningText = popView.findViewById<TextView>(R.id.winningText)//sets text

        popUpWinWindow.showAtLocation(popView, Gravity.CENTER, 0, 0)
        if (!playerWon) {
            //if computer wins show below text in red
            winningText.text = "You lose"
            winningText.setTextColor(Color.RED)
            comWins++


        } else {
            //if player wins show below text in green
            winningText.text = "You win"
            winningText.setTextColor(Color.GREEN)
            playerWins++
        }

        popView.setOnTouchListener { v, event ->
            popUpWinWindow.dismiss()
            true
        }
    }

    private fun generateRandomDice() {
        //function to generate random dice


        for (i in 0..indexSumOfDice) {
            val gen: Random = Random
            //on click of die the below array index will change
            if (!chosenDiceArray[i]) {
                //if the index value of the die is not true then randomize it
                val dieNumber = gen.nextInt(6)
                imgArrayPlayer[i].setImageResource(dicePicArray[dieNumber])
                imgArrayPlayer[i].animate().rotation(imgArrayPlayer[i].rotation + 360).duration =
                    100
                //animation
                dieValuesPlayer[i] = dieNumber + 1 //replace the index value of the array with new value


            }

        }
        for (k in 0..indexSumOfDice)
            //reset the array as all false now
            chosenDiceArray[k] = false
    }

    private fun reRollComputerDie() {
        val generateNo: Random = Random//
        val uniqueDie = mutableListOf<Int>()
        val noOfDiceToKeep = generateNo.nextInt(5)//generates the number of dice to keep
        var chosenIndex = generateNo.nextInt(5)//generates a random index
        while (uniqueDie.size != noOfDiceToKeep) {//till the number of unique index are
            // equal to the size of the no of chosen indexes
            if (chosenIndex !in uniqueDie) {
                uniqueDie.add(chosenIndex)
            } else
                chosenIndex = generateNo.nextInt(5)//if not generate another index
        }
        for (k in 0..4) {
            if (uniqueDie.contains(k)) {// if the index value matches do the below
                val newDiceValue = generateNo.nextInt(6)//generates the new die value
                imgArrayComputer[k].setImageResource(dicePicArray[newDiceValue])
                //sets the image as the new value
                imgArrayComputer[k].animate()
                    .rotation(imgArrayComputer[k].rotation + 360).duration = 100
                //small rotation animation
                dieValueComputer[k] = newDiceValue + 1
            }
        }

        uniqueDie.clear()//clears the array

    }


    private fun generateRandomDiceCom() {
        //simply generate a random number till 5 and set image resource according to that index
        for (i in 0..4) {
            val gen: Random = Random
            val dieNumber = gen.nextInt(6)
            imgArrayComputer[i].setImageResource(dicePicArray[dieNumber])
            //slight animation of rolling
                imgArrayComputer[i].animate()
                .rotation(imgArrayComputer[i].rotation + 360).duration = 100
            dieValueComputer[i] = dieNumber + 1 //sets new value

        }


    }


    @SuppressLint("SetTextI18n")
    private fun score(
        //updates the score accordingly
        txtScore: TextView, btnThrow: Button,
        txtMainMiddle: TextView, txtTopMiddle: TextView, btnScore: Button
    ) {
        if (gameStarted){


        for (i in 0..indexSumOfDice){
            imgArrayPlayer[i].isClickable=false
        }

        scorePressed = true
        btnThrow.isVisible = true
        roundCount++ //update round count
        reRollCount = 0 // sets re roll count back to zero



        for (i in 0..4) {
            unHighlightDice(imgArrayPlayer[i])
            unHighlightDice(imgArrayComputer[i])
            chosenDiceArray[i] = false
            //un highlights the dice

        }

        computerReRollCount = 0//sets computer reroll count to zero
        scoreCom += dieValueComputer.sum()
        scorePlayer += dieValuesPlayer.sum()//adds the sum to the score
        updateMidText(txtTopMiddle, txtMainMiddle)
        txtScore.text = "Computer $scoreCom \n Player $scorePlayer "
        //update the score


        if (scorePlayer == winningScore && scoreCom == winningScore) {
            //special condition if tie occurs
            Toast.makeText(this, "Tie.Only throws. no re rolls", Toast.LENGTH_SHORT).show()

        } else {
            if (scorePlayer >= winningScore && scoreCom >= winningScore) {
                if (scorePlayer > scoreCom) {
                    //if the player won
                    winPopup(txtMainMiddle, true, btnScore, btnThrow)


                } else if (scoreCom > scorePlayer) {
                    //if the computer won
                    winPopup(txtMainMiddle, false, btnScore, btnThrow)


                }
                //below is done in case one value is less than the winning score
            } else if (scorePlayer >= winningScore) {
                winPopup(txtMainMiddle, true, btnScore, btnThrow)

            } else if (scoreCom >= winningScore) {
                winPopup(txtMainMiddle, false, btnScore, btnThrow)


            }


        }
       // updateMidText(txtTopMiddle, txtMainMiddle)
        btnScore.isClickable = false //sets button to not clickable


        //   updateMidText(txtTopMiddle, txtMainMiddle)
            //change 1


    }}

    override fun onSaveInstanceState(outState: Bundle) {
        //sets the needed variables into the save instance state
        super.onSaveInstanceState(outState)
        outState.putBoolean("throwPressed",throwPressed)
        outState.putBoolean("scorePressed",scorePressed)
        outState.putInt("scoreCom", scoreCom)
        outState.putInt("scorePlayer", scorePlayer)
        outState.putInt("reRollCount", reRollCount)
        outState.putInt("winningScore", winningScore)
        outState.putInt("roundCount", roundCount)
        outState.putInt("computerRerollCount", computerReRollCount)
        outState.putIntArray("diePlayernow", dieValuesPlayer)
        outState.putIntArray("dieComputerNow", dieValueComputer)
        outState.putBoolean("chosenDice1", chosenDiceArray[0])
        outState.putBoolean("chosenDice2", chosenDiceArray[1])
        outState.putBoolean("chosenDice3", chosenDiceArray[2])
        outState.putBoolean("chosenDice4", chosenDiceArray[3])
        outState.putBoolean("chosenDice5", chosenDiceArray[4])
        outState.putBoolean("gameWon", gameWon)
        outState.putBoolean("gameStarted",gameStarted)





    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //restroes the above sets vraible to the neede values
        super.onRestoreInstanceState(savedInstanceState)
        gameWon=savedInstanceState.getBoolean("gameWon")
        gameStarted=savedInstanceState.getBoolean("gameStarted")
        throwPressed=savedInstanceState.getBoolean("throwPressed",false)
        scorePressed=savedInstanceState.getBoolean("scorePressed",false)
        scoreCom = savedInstanceState.getInt("scoreCom", 101)
        scorePlayer = savedInstanceState.getInt("scorePlayer", 101)
        reRollCount = savedInstanceState.getInt("reRollCount", 1)
        winningScore = savedInstanceState.getInt("winningScore", 101)
        computerReRollCount = savedInstanceState.getInt("computerRerollCount")
        roundCount = savedInstanceState.getInt("roundCount")

        dieValuesPlayer = savedInstanceState.getIntArray("diePlayernow")!!
        dieValueComputer = savedInstanceState.getIntArray("dieComputerNow")!!
        chosenDiceArray[0] = savedInstanceState.getBoolean("chosenDice1")
        chosenDiceArray[1] = savedInstanceState.getBoolean("chosenDice2")
        chosenDiceArray[2] = savedInstanceState.getBoolean("chosenDice3")
        chosenDiceArray[3] = savedInstanceState.getBoolean("chosenDice4")
        chosenDiceArray[4] = savedInstanceState.getBoolean("chosenDice5")
        Toast.makeText(this, "Activity restored", Toast.LENGTH_SHORT).show()
        for (i in 0..4) {
            if (dieValueComputer[i] != 0)
                imgArrayComputer[i].setImageResource(dicePicArray[dieValueComputer[i] - 1])

            if (dieValuesPlayer[i] != 0)
                imgArrayPlayer[i].setImageResource(dicePicArray[dieValuesPlayer[i] - 1])
            if (chosenDiceArray[i]) {

                highlightDie(imgArrayPlayer[i], false)
            }



        }
    }


    private fun unHighlightDice(imageView: ImageView) {
        imageView.setBackgroundColor(Color.TRANSPARENT)
        imageView.setPadding(0, 0, 0, 0)
        //un highlight dice by setting padding to zero and setting background as transparent
    }


    private fun highlightDie(imageView: ImageView, boolean: Boolean) {
        //highlight dice automatically according to their boolean is clicked

        if (!boolean) {
            imageView.setBackgroundColor(Color.GREEN)
            imageView.setPadding(10, 10, 10, 10)
        } else {
            imageView.setBackgroundColor(Color.TRANSPARENT)
            imageView.setPadding(0, 0, 0, 0)
        }


    }

    private fun generateComputerStrategy() {

        computerReRollCount = 0
        val gen: Random = Random
        for (i in 0..4) {

            val dieNumber = gen.nextInt(6)
            imgArrayComputer[i].setImageResource(dicePicArray[dieNumber])
            dieValueComputer[i] = dieNumber + 1
        }
        //if the sum can beat the wining score only re roll die value 1
        if ((scoreCom + dieValueComputer.sum()) >= winningScore) {
            for (p in dieValueComputer) {
                if (p == 1) {
                    val dieNumber = gen.nextInt(6)
                    imgArrayComputer[p].setImageResource(dicePicArray[dieNumber])
                    dieValueComputer[p] = dieNumber + 1


                }
            }

        } else {
            while (computerReRollCount < 2) {
                computerReRollCount++
                if (scoreCom>=scorePlayer) {
                    //if in a leading position take less risk and only roll dies value 1 and 2
                    for (i in dieValueComputer) {
                        if (i <= 2) {
                            val dieNumber = gen.nextInt(6)
                            imgArrayComputer[i].setImageResource(dicePicArray[dieNumber])
                            dieValueComputer[i] = dieNumber + 1
                            if ((winningScore + dieValueComputer.sum()) >= winningScore) {

                                break
                            }

                        }


                    }
                } else if (scoreCom < scorePlayer) {
                    //if in a losing position take more risk and roll die value 1,2 and 3
                    for (i in dieValueComputer) {
                        if (i <= 3) {
                            val dieNumber = gen.nextInt(6)
                            imgArrayComputer[i].setImageResource(dicePicArray[dieNumber])
                            dieValueComputer[i] = dieNumber + 1

                        }
                    }


                }
            }
        }


    }


}

