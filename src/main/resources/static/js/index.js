import Window from './window.js'

window.templateController = new Window(
    'target', 'scoreTemplate',
    'commentTemplate', 'ratingTemplate'
);

document.sendComment = () => {
    const comment = document.getElementById('comment-text-area').value;
    if(comment.trim() === ''){
        return
    }
    document.getElementById('comment-text-area').value = '';
    const player = document.getElementById('user').textContent.substring(6)
    const data = {
        player: player,
        comment: comment,
        game: 'tentrix',
        commentedOn: Date.now()
    }
    console.log(data)
    fetch('http://localhost:8080/api/comment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    }).then(response => {
        console.log(response);
        if(response.ok){
            window.templateController.showComments();
        }
    })
}

document.setRating = () => {
    const rating_str = document.getElementById('set-rating').value.trim();
    if(isNaN(rating_str)){
        return;
    }
    const rating = parseInt(rating_str);
    if(rating < 1 || rating > 5){
        return;
    }
    const player = document.getElementById('user').textContent.substring(6)
    const data = {
        game: 'tentrix',
        player: player,
        rating: rating,
        ratedOn: Date.now()
    }
    console.log(data)
    fetch('http://localhost:8080/api/rating', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    }).then(response => {
        if(response.ok){
            window.templateController.showRating();
        }
    })
}