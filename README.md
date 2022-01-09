# LedBuzz
<p align="center"><img src="https://github.com/himanshushah05/LedBuzz/blob/main/LedBuzz.png"/></p>

## What it does
<p> LedBuzz is a NodeMCU-powered see-through propeller display. By simply staring at your ceiling fan, you receive notification alert. You can use the app to add future alarms. After you've entered the information, you'll see the text message on the fan and receive a notification on your phone. We even have a website that can be used to sync the device as per the motor's RPM. </p>


## How we built it
We teamed up first . Divided work of Android Dev and Electronics  . The Electronics team made the hardware and write code for hardware funcationality and after we were done we all organised a meet to integrate the components.
## How to run it locally
![image](https://user-images.githubusercontent.com/78071859/148664538-45368c92-b2d6-4540-b29a-91d7f5feb47e.png)

- Make the connections on perf board as per the above circuit diagram.   
- Assemble the batteries and NodeMCU such that the weight of the perf board is balanced.
- Open the (arduino code repo link) and add your firebase account host URL and authorization token and SSID and password of your local network.
- Clone the repo.
  `git clone https://github.com/himanshushah05/LedBuzz`
- Checkout to a new branch.
  `git checkout -b my-amazing-feature`
- Make some amazing changes.
- `git add .`
- `git commit -m "<Verb>: <Action>"`
- `git push origin my-amazing-feature`
- Open a pull request :)
- Visit https://github.com/himanshushah05/LedBuzz for arduino code.

## App UI
<table>
    <tr>
      <td>
        <img width="250px" src="https://github.com/himanshushah05/LedBuzz/blob/main/App1.jpeg">
      </td>
      <td>
        <img width="250px" src="https://github.com/himanshushah05/LedBuzz/blob/main/App2.jpeg">
      </td>
      <td>
        <img width="250px" src="https://github.com/himanshushah05/LedBuzz/blob/main/App3.jpeg">
      </td>
      <td>
        <img width="250px" src="https://github.com/himanshushah05/LedBuzz/blob/main/App4.jpeg">
      </td>
      <td>
        <img width="250px" src="https://github.com/himanshushah05/LedBuzz/blob/main/App5.jpeg">
      </td>
      
    
  </table>
  
## Challenges we ran into
One of the common problem that i run into in all the hackathons including this is that it becomes really difficult to integrate the moving parts at the last moment.
Hardware Limitations 
Node MCU firebase library issue
Soldering the LED connection 
Physical Connection to fan :(
Calibrating RPM without any IR sensor :( 
## Accomplishments that we're proud of
Making this complex peoduct within 24 hrs with several moving part is what I am proud of.
## What we learned
We learnt work in team. We followed the git protocol like feature branching relegiously. 
## Important URLs
- Git hub repo link for arduino code: https://github.com/himanshushah05/LedBuzz
