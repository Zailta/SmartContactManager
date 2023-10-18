console.log('Hello');
const toggleSideBar = () => {
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%" );
	}else{
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "18%" );
	}
};

const search = () => {
	let query= $("#search-input").val();
	if(query==''){
		$(".search-results").hide();
	}
	else{
		let url = `http://localhost:8082/contactManager/search/${query}`;
		fetch(url)
		.then((response) =>{
			return response.json();
		})
		.then((data) =>{
			let text = `<div class='list-group'>`;
			data.forEach(contact => {
				text+=`<a href='/contactManager/user/view-contact/${contact.contactID}' class='list-group-item list-group-action'>${contact.name}</a>`;
			});
			text+=`</div>`
			$(".search-results").html(text);
			$(".search-results").show();
		});
		
	}
};