#
# Set user to root, and change dir
#
# First do this
# sudo su -
# cd /home/webuser/dev/proj/spring-examples/multitenantjdbc/db-env/scripts
#
# then set prompt using . ./set-prompt.sh
#
export PS1="\[\e]0;\u@\h: \w\a\]${debian_chroot:+($debian_chroot)}\u@\h:\w\n\$ "
echo "Set prompt"

