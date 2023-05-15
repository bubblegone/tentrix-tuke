import Mustache from './mustache.js';

export default class Window {
    url = 'http://localhost:8080'

    constructor(targetID, scoreTemplateID, commentTemplateID, ratingTemplateID) {
        this.target = document.getElementById(targetID);
        this.scoreTemplate = document.getElementById(scoreTemplateID);
        this.commentTemplate = document.getElementById(commentTemplateID);
        this.ratingTemplate = document.getElementById(ratingTemplateID);
    }

    render(renderObj, template) {
        const hasRecords = renderObj.length > 0;
        this.target.innerHTML = Mustache.render(template.innerHTML, {
            hasRecords: hasRecords,
            data: renderObj.reverse()
        });
        this.target.style.opacity = '1';
        this.target.style.zIndex = '1';
        document.getElementById('main').style.opacity = '0';
    }

    showScoreboard() {
        fetch(this.url + '/api/score/tentrix').then(response => {
            console.log(response)
            if (response.ok) {
                return response.json()
            } else {
                this.displayError();
            }
        }).then(responseJSON => {
            console.log(responseJSON);
            const objArray = responseJSON.map(obj => {
                let dateSplit = obj.playedAt.split(/[T.]/)
                return {
                    game: obj.game,
                    player: obj.player,
                    points: obj.points,
                    playedOn: dateSplit[0],
                    playedAt: dateSplit[1]
                }
            });
            this.render(objArray, this.scoreTemplate);
        }).catch(error => {
            console.log(error);
            this.displayError();
        })
    }

    showComments() {
        fetch(this.url + '/api/comment/tentrix').then(response => {
            if (response.ok) {
                return response.json()
            } else {
                this.displayError();
            }
        }).then(responseJSON => {
            const objArray = responseJSON.map(obj => {
                let dateSplit = obj.commentedOn.split(/[T.]/)
                return {
                    game: obj.game,
                    player: obj.player,
                    comment: obj.comment,
                    commentedOn: dateSplit[0],
                    commentedAt: dateSplit[1]
                }
            });
            this.render(objArray, this.commentTemplate)
        }).catch(error => {
            console.log(error);
            this.displayError();
        })
    }

    async showRating() {
        let avgRating, myRating;
        await fetch(this.url + '/api/rating/tentrix/avg').then(response => {
            if (response.ok) {
                return response.text();
            } else {
                this.displayError();
            }
        }).then(responseTXT => {
            avgRating = '*'.repeat(responseTXT)
        }).catch(error => {
            this.displayError();
        })

        const player = document.getElementById('user').textContent.substring(6)
        await fetch(this.url + '/api/rating/tentrix/player/' + player).then(response => {
            if (response.ok) {
                return response.text();
            } else {
                this.displayError();
            }
        }).then(responseTXT => {
            myRating = '*'.repeat(responseTXT)
        }).catch(error => {
            this.displayError();
        })

        const objArray = [{
            avgRating: avgRating,
            myRating: myRating
        }]

        this.render(objArray, this.ratingTemplate)
    }

    hide() {
        this.target.innerHTML = '';
        this.target.style.opacity = '0';
        this.target.style.zIndex = '-1';
        document.getElementById('main').style.opacity = '1';
    }

    displayError() {
        this.target.innerHTML = '<h1>Unspecified Error happened</h1>'
    }
}