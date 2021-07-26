#!/bin/bash

ssh root@167.71.50.44 'rm -r /home/chatroomAPI'
scp -r ~/Downloads/FinalProjectGML root@167.71.50.44:/home/chatroomAPI
ssh root@167.71.50.44 'bash /home/chatroomAPI/docker-run.sh'