#!/bin/bash

set -u
RPTOOLS_ROOT=~/projects/3rd_party/rptools_new
source "$RPTOOS_ROOT""/bashvars"

for project in $projects ; do
    dest="$RPTOOS_ROOT""/""$project"
    rm -rf "$dest"
    repo_url="git@github.com:RPTools/""$project"".git"
    git clone "$repo_url"
    cd "$dest"
    git remote add upstream "$repo_url"
    git fetch upstream
    git checkout master
    git merge upstream/master
done
