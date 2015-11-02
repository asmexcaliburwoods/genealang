Described [here](http://www.subversionary.org/howto/setting-up-a-subversion-server-on-ubuntu-gutsy-gibbon-server). My (very rudimentary) comments are:

You can use `jstar` editor to edit `/etc/passwd` and `/etc/group` instead of `vipw` and `vigr`. jstar has a familiar Borland `WordStar` keyboard interface.

The following installs `jstar`:

`sudo aptitude install joe`

The following can be used to edit passwd and group:

`sudo -i`

`jstar /etc/passwd`

`jstar /etc/group`

Remove backups after editing:

`rm /etc/passwd~`

`rm /etc/group~`