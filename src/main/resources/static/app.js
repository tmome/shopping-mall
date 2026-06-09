const productForm = document.querySelector("#productForm");
const productList = document.querySelector("#productList");
const refreshButton = document.querySelector("#refreshButton");
const message = document.querySelector("#message");
const apiStatus = document.querySelector("#apiStatus");
const authStatus = document.querySelector("#authStatus");
const loginActions = document.querySelector("#loginActions");
const logoutForm = document.querySelector("#logoutForm");

const moneyFormatter = new Intl.NumberFormat("ko-KR", {
	style: "currency",
	currency: "KRW",
	maximumFractionDigits: 0,
});

async function request(path, options = {}) {
	const response = await fetch(path, {
		headers: {
			"Content-Type": "application/json",
			...options.headers,
		},
		...options,
	});

	if (!response.ok) {
		const body = await response.text();
		throw new Error(body || `HTTP ${response.status}`);
	}

	return response.json();
}

function setMessage(text, isError = false) {
	message.textContent = text;
	message.classList.toggle("error", isError);
}

function setApiStatus(text, status) {
	apiStatus.textContent = text;
	apiStatus.className = `status ${status || ""}`.trim();
}

function renderProducts(products) {
	if (products.length === 0) {
		productList.innerHTML = `<div class="empty">등록된 상품이 없습니다.</div>`;
		return;
	}

	productList.innerHTML = products
		.map((product) => {
			const price = moneyFormatter.format(Number(product.price));
			return `
				<article class="product">
					<div>
						<strong>${escapeHtml(product.name)}</strong>
						<span>재고 ${product.stockQuantity.toLocaleString("ko-KR")}개 · ID ${product.id}</span>
					</div>
					<div class="price">${price}</div>
				</article>
			`;
		})
		.join("");
}

function escapeHtml(value) {
	return String(value)
		.replaceAll("&", "&amp;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;")
		.replaceAll('"', "&quot;")
		.replaceAll("'", "&#039;");
}

async function loadProducts() {
	refreshButton.disabled = true;
	try {
		const products = await request("/api/products");
		renderProducts(products);
		setApiStatus("API 연결됨", "ok");
	} catch (error) {
		renderProducts([]);
		setApiStatus("API 오류", "error");
		setMessage("상품 목록을 불러오지 못했습니다.", true);
	} finally {
		refreshButton.disabled = false;
	}
}

async function loadAuth() {
	try {
		const me = await request("/api/auth/me");
		if (me.authenticated) {
			authStatus.textContent = `${me.name} 로그인됨`;
			loginActions.hidden = true;
			logoutForm.hidden = false;
			return;
		}

		authStatus.textContent = "로그인 필요";
		loginActions.hidden = false;
		logoutForm.hidden = true;
	} catch (error) {
		authStatus.textContent = "로그인 상태 확인 실패";
		loginActions.hidden = false;
		logoutForm.hidden = true;
	}
}

productForm.addEventListener("submit", async (event) => {
	event.preventDefault();

	const submitButton = productForm.querySelector("button");
	submitButton.disabled = true;
	setMessage("");

	const formData = new FormData(productForm);
	const payload = {
		name: formData.get("name").trim(),
		price: Number(formData.get("price")),
		stockQuantity: Number(formData.get("stockQuantity")),
	};

	try {
		await request("/api/products", {
			method: "POST",
			body: JSON.stringify(payload),
		});
		productForm.reset();
		setMessage("상품을 등록했습니다.");
		await loadProducts();
	} catch (error) {
		if (error.message.includes("HTTP 401")) {
			setMessage("상품 등록은 로그인 후 사용할 수 있습니다.", true);
		} else {
			setMessage("상품 등록에 실패했습니다. 입력값을 확인하세요.", true);
		}
	} finally {
		submitButton.disabled = false;
	}
});

refreshButton.addEventListener("click", () => {
	setMessage("");
	loadProducts();
});

loadAuth();
loadProducts();
