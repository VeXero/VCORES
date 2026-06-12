"use client";

import { useState } from "react";

const portfolioPlaceholders = [
	{
		id: 1,
		title: "SPAMTON In Drag",
		img: "/portfolio/1.png",
		meta: "[Painting] [Half-Body]",
	},
	{
		id: 2,
		title: "Valentines Day",
		img: "/portfolio/2.png",
		meta: "[Render] [Full-Body]",
	},
	{
		id: 3,
		title: "Diva SPAMTON",
		img: "/portfolio/3.png",
		meta: "[Render] [Full-Body]",
	},
	{
		id: 4,
		title: "UNDYNE",
		img: "/portfolio/4.png",
		meta: "[Render] [Headshot]",
	},
	{
		id: 5,
		title: "3 AM",
		img: "/portfolio/5.png",
		meta: "[Render] [Mixed-Body]",
	},
	{
		id: 6,
		title: "Memories",
		img: "/portfolio/6.png",
		meta: "[Render] [Half-Body]",
	},
];

const commissionCategories = [
	{
		name: "Sketch",
		price: "$5 - $15",
		details: "Slightly messy linework.",
	},
	{
		name: "Simple Color",
		price: "$15 - $30",
		details:
			"More polished linework (in my style) and slightly shaded base colors.",
	},
	{
		name: "Full Render",
		price: "$30 - $80*",
		details:
			"Detailed lighting, effects, and finish. *Price varies widely based on complexity.",
	},
	{
		name: "Custom Project",
		price: "Quote Based",
		details: "Large scenes, banners, branding art, and commercial requests.",
	},
];

const faqs = [
	{
		q: "What should I send when I request a commission?",
		a: "Please share references, character details, mood, pose ideas, and your deadline.",
	},
	{
		q: "How long does a commission usually take?",
		a: "Typical delivery is 3 to 14 days depending on complexity and queue length.",
	},
	{
		q: "Do you offer revisions?",
		a: "Yes. Most orders include one major revision at sketch stage and minor polish tweaks.",
	},
	{
		q: "Can I request commercial usage rights?",
		a: "Yes, commercial rights are available as an add-on. Pricing depends on usage scope.",
	},
];

export default function Home() {
	const [senderName, setSenderName] = useState("");
	const [senderEmail, setSenderEmail] = useState("");
	const [commissionType, setCommissionType] = useState("HEADSHOT");
	const [artstyleType, setArtstyleType] = useState("Sketch");
	const [additionalNotes, setAdditionalNotes] = useState("");
	const [status, setStatus] = useState("");

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		setStatus("Sending...");
		try {
			const response = await fetch("http://localhost:8080/api/commissions", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					senderName,
					senderEmail,
					commissionType,
					artstyleType,
					additionalNotes,
				}),
			});
			if (response.ok) {
				setStatus("Commission sent successfully! Thank you!");
				setSenderName("");
				setSenderEmail("");
				setCommissionType("HEADSHOT");
				setArtstyleType("Sketch");
				setAdditionalNotes("");
			} else {
				setStatus("Failed to send commission. Please try again.");
			}
		} catch (e) {
			console.error(e);
			setStatus("Error sending commission.");
		}
	};

	const [renderTab, setRenderTab] = useState("price");
	const [sketchTab, setSketchTab] = useState("price");
	const [simpleTab, setSimpleTab] = useState("price");

	const scrollToContact = () => {
		document.getElementById("contact")?.scrollIntoView({ behavior: "smooth" });
	};

	return (
		<main className="mx-auto flex w-full max-w-6xl flex-col gap-10 px-4 py-8 sm:px-6 lg:px-8">
			{/* Welcome Section */}
			<section className="rounded-xl border border-zinc-200 bg-zinc-50 p-6 shadow-sm dark:border-zinc-800 dark:bg-zinc-900/60">
				<p className="mb-2 text-sm uppercase tracking-[0.2em] text-zinc-500">
					Portfolio and Commission website
				</p>
				<div className="title-bar">
					<div className="title-bar-text">Welcome!</div>
					<div className="title-bar-controls">
						<button aria-label="Minimize"></button>
						<button aria-label="Restore"></button>
						<button aria-label="Close"></button>
					</div>
				</div>

				<h1 className="text-4xl font-bold tracking-tight sm:text-5xl">
					VCORE
				</h1>
				<p className="mt-3 max-w-3xl text-zinc-600 dark:text-zinc-300">
					I do art :3
				</p>

				<div className="mt-6 flex flex-wrap gap-3">
					<button
						type="button"
						onClick={scrollToContact}
						className="rounded-none border border-zinc-800 bg-zinc-200 px-4 py-2 text-sm font-semibold text-zinc-900 shadow-sm hover:bg-zinc-300"
					>
						Request a Commission
					</button>
					<button className="rounded-none border border-zinc-800 bg-zinc-200 px-4 py-2 text-sm font-semibold text-zinc-900 shadow-sm hover:bg-zinc-300">
						View Portfolio
					</button>
				</div>
			</section>

			<div className="window">
				<div className="title-bar">
					<div className="title-bar-text">Commission Info</div>
					<div className="title-bar-controls">
						<button aria-label="Minimize" />
						<button aria-label="Maximize" />
						<button aria-label="Close" />
					</div>
				</div>

				<div className="window-body">
					<pre>
						{`C:VEXCORES\\PORTFOLIO\\EXAMPLES>

PORTFOLIO
`}
						<div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
							{portfolioPlaceholders.map((item) => (
								<article
									key={item.id}
									className="rounded-lg border border-dashed border-zinc-300 p-4 dark:border-zinc-700"
								>
									<div className="mb-3 aspect-[1/1] overflow-hidden rounded-md bg-zinc-900">
										<img
											src={item.img}
											alt={item.title}
											className="h-full w-full object-cover"
										/>
									</div>
									<h3 className="font-medium">{item.title}</h3>
									<p className="mt-1 text-sm text-zinc-500">
										{item.meta}
									</p>
								</article>
							))}
						</div>
					</pre>
				</div>
			</div>

			<section className="rounded-xl border border-zinc-200 p-6 dark:border-zinc-800">
				<div className="title-bar">
					<div className="title-bar-text">
						Commission Prices by Category TESTING
					</div>
					<div className="title-bar-controls">
						<button aria-label="Help"></button>
					</div>
				</div>

				<div className="flex flex-wrap gap-4 justify-start">
					{/* RENDER Window */}
					<div className="window flex-1 min-w-[320px] max-w-[350px]">
						<div className="title-bar">
							<div className="title-bar-text">RENDER</div>
							<div className="title-bar-controls">
								<button aria-label="Minimize"></button>
								<button aria-label="Maximize"></button>
								<button aria-label="Close"></button>
							</div>
						</div>
						<div className="window-body">
							<menu role="tablist" className="mb-4 flex gap-2">
								<button
									aria-selected={renderTab === "price"}
									aria-controls="price"
									className="px-2 py-1 rounded border"
									onClick={() => setRenderTab("price")}
								>
									PRICE
								</button>
								<button
									aria-selected={renderTab === "examples"}
									aria-controls="EXAMPLES"
									className="px-2 py-1 rounded border"
									onClick={() => setRenderTab("EXAMPLES")}
								>
									EXAMPLES
								</button>
								<button
									aria-selected={renderTab === "expect"}
									aria-controls="EXPECT"
									className="px-2 py-1 rounded border"
									onClick={() => setRenderTab("EXPECT")}
								>
									TO EXPECT
								</button>
							</menu>

							{/* Price Tab */}
							<article role="tabpanel" id="price" hidden={renderTab !== "price"}>
								<p>Select your request</p>
								<fieldset>
									<legend>Available options</legend>
									<div className="field-row">
										<input id="radio29" type="radio" name="fieldset-example2" />
										<label htmlFor="SKHS">Headshot</label>
									</div>
									<div className="field-row">
										<input id="radio30" type="radio" name="fieldset-example2" />
										<label htmlFor="SKT">Torso</label>
									</div>
									<div className="field-row">
										<input id="radio31" type="radio" name="fieldset-example2" />
										<label htmlFor="SKFB">Full body</label>
									</div>
								</fieldset>
								<section className="field-row">
									<button>Curiosity button.</button>
									<label>Try this to get some attention</label>
								</section>
							</article>

							{/* Examples Tab */}
							<article role="tabpanel" id="EXAMPLES" hidden={renderTab !== "EXAMPLES"}>
								<img style={{ width: "100%" }} src="/RENDER1.png" />
								<img style={{ width: "100%" }} src="/RENDER2.png" />
								<img style={{ width: "100%" }} src="/RENDER3.png" />
							</article>

							{/* TO EXPECT Tab */}
							<article role="tabpanel" id="EXPECT" hidden={renderTab !== "EXPECT"}>
								<iframe
									width="289"
									height="315"
									src="https://www.youtube.com/embed/t76jz7iFiAY?si=VjMrVCqwK08BRVIz"
									title="YouTube video player"
									frameBorder="0"
									allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
									referrerPolicy="strict-origin-when-cross-origin"
									allowFullScreen
								></iframe>
								<img style={{ width: "100%" }} src="/GeorgeDURR.jpg" />
							</article>

							<section
								className="field-row"
								style={{ justifyContent: "flex-end" }}
							>
								<button>OK</button>
								<button>Cancel</button>
							</section>
						</div>
					</div>

					{/* SIMPLE Window */}
					<div className="window flex-1 min-w-[320px] max-w-[350px]">
						<div className="title-bar">
							<div className="title-bar-text">SIMPLE</div>
							<div className="title-bar-controls">
								<button aria-label="Minimize"></button>
								<button aria-label="Maximize"></button>
								<button aria-label="Close"></button>
							</div>
						</div>

						<div className="window-body">
							<menu role="tablist" className="mb-4 flex gap-2">
								<button
									aria-selected={simpleTab === "price"}
									aria-controls="price"
									className="px-2 py-1 rounded border"
									onClick={() => setSimpleTab("price")}
								>
									PRICE
								</button>
								<button
									aria-selected={simpleTab === "examples"}
									aria-controls="EXAMPLES"
									className="px-2 py-1 rounded border"
									onClick={() => setSimpleTab("EXAMPLES")}
								>
									EXAMPLES
								</button>
								<button
									aria-selected={simpleTab === "expect"}
									aria-controls="EXPECT"
									className="px-2 py-1 rounded border"
									onClick={() => setSimpleTab("EXPECT")}
								>
									TO EXPECT
								</button>
							</menu>

							{/* Price Tab */}
							<article role="tabpanel" id="price" hidden={simpleTab !== "price"}>
								<p>Select your request</p>
								<fieldset>
									<legend>Available options</legend>

									<div className="field-row">
										<input id="radio38" type="radio" name="fieldset-example4" />
										<label htmlFor="SMHS">Headshot</label>
									</div>

									<div className="field-row">
										<input id="radio39" type="radio" name="fieldset-example4" />
										<label htmlFor="SMT">Torso</label>
									</div>

									<div className="field-row">
										<input id="radio40" type="radio" name="fieldset-example4" />
										<label htmlFor="SMFB">Full body</label>
									</div>
								</fieldset>

								<section className="field-row">
									<button>Curiosity button.</button>
									<label>Try this to get some attention</label>
								</section>
							</article>

							{/* Examples Tab */}
							<article role="tabpanel" id="EXAMPLES" hidden={simpleTab !== "EXAMPLES"}>
								<img style={{ width: "100%" }} src="/SIMPLE1.png" />
								<img style={{ width: "100%" }} src="/SIMPLE2.png" />
								<img style={{ width: "100%" }} src="/SIMPLE3.png" />
							</article>

							{/* TO EXPECT Tab */}
							<article role="tabpanel" id="EXPECT" hidden={simpleTab !== "EXPECT"}>
								<p>Expect info for SIMPLE.</p>
							</article>

							<section
								className="field-row"
								style={{ justifyContent: "flex-end" }}
							>
								<button>OK</button>
								<button>Cancel</button>
							</section>
						</div>
					</div>

					{/* SKETCH Window */}
					<div className="window flex-1 min-w-[320px] max-w-[350px]">
						<div className="title-bar">
							<div className="title-bar-text">SKETCH</div>
							<div className="title-bar-controls">
								<button aria-label="Minimize"></button>
								<button aria-label="Maximize"></button>
								<button aria-label="Close"></button>
							</div>
						</div>
						<div className="window-body">
							<menu role="tablist" className="mb-4 flex gap-2">
								<button
									aria-selected={sketchTab === "price"}
									aria-controls="price"
									className="px-2 py-1 rounded border"
									onClick={() => setSketchTab("price")}
								>
									PRICE
								</button>
								<button
									aria-selected={sketchTab === "examples"}
									aria-controls="EXAMPLES"
									className="px-2 py-1 rounded border"
									onClick={() => setSketchTab("EXAMPLES")}
								>
									EXAMPLES
								</button>
								<button
									aria-selected={sketchTab === "expect"}
									aria-controls="EXPECT"
									className="px-2 py-1 rounded border"
									onClick={() => setSketchTab("EXPECT")}
								>
									TO EXPECT
								</button>
							</menu>

							{/* Price Tab */}
							<article role="tabpanel" id="price" hidden={sketchTab !== "price"}>
								<p>Select your request</p>
								<fieldset>
									<legend>Available options</legend>
									<div className="field-row">
										<input id="radio35" type="radio" name="fieldset-example3" />
										<label htmlFor="SKHS">Headshot</label>
									</div>
									<div className="field-row">
										<input id="radio36" type="radio" name="fieldset-example3" />
										<label htmlFor="SKT">Torso</label>
									</div>
									<div className="field-row">
										<input id="radio37" type="radio" name="fieldset-example3" />
										<label htmlFor="SKFB">Full body</label>
									</div>
								</fieldset>
								<section className="field-row">
									<button>Curiosity button.</button>
									<label>Try this to get some attention</label>
								</section>
							</article>

							{/* Examples Tab */}
							<article role="tabpanel" id="EXAMPLES" hidden={sketchTab !== "EXAMPLES"}>
								<img style={{ width: "100%" }} src="/SKETCH1.png" />
								<img style={{ width: "100%" }} src="/SKETCH2.png" />
								<img style={{ width: "100%" }} src="/SKETCH3.png" />
							</article>

							{/* TO EXPECT Tab */}
							<article role="tabpanel" id="EXPECT" hidden={sketchTab !== "EXPECT"}>
								<p>Expect info for SKETCH.</p>
							</article>

							<section
								className="field-row"
								style={{ justifyContent: "flex-end" }}
							>
								<button>OK</button>
								<button>Cancel</button>
							</section>
						</div>
					</div>
				</div>
			</section>

			{/* Commission Prices Section */}
			<section className="rounded-xl border border-zinc-200 p-6 dark:border-zinc-800">
				<h2 className="mb-5 text-2xl font-semibold">
					Commission Prices by Category
				</h2>
				<div className="grid gap-4 md:grid-cols-2">
					{commissionCategories.map((category) => (
						<article
							key={category.name}
							className="rounded-lg border border-zinc-200 bg-zinc-50 p-4 dark:border-zinc-700 dark:bg-zinc-900/50"
						>
							<div className="flex items-baseline justify-between gap-2">
								<h3 className="text-lg font-semibold">{category.name}</h3>
								<span className="text-sm font-medium text-zinc-600 dark:text-zinc-300">
									{category.price}
								</span>
							</div>
							<p className="mt-2 text-sm text-zinc-500">
								{category.details}
							</p>
							<p className="mt-3 text-xs uppercase tracking-wide text-zinc-500">
								Placeholder add-ons: extra character, background, rush delivery
							</p>
						</article>
					))}
				</div>
			</section>

			{/* How It Works */}
			<section className="grid gap-4 rounded-xl border border-zinc-200 p-6 dark:border-zinc-800 md:grid-cols-3">
				<h2 className="md:col-span-3 text-2xl font-semibold">How It Works</h2>
				<article className="rounded-lg border border-zinc-200 p-4 dark:border-zinc-700">
					<p className="text-sm font-semibold text-zinc-500">Step 1</p>
					<h3 className="mt-1 font-semibold">Send Request</h3>
					<p className="mt-2 text-sm text-zinc-500">
						Describe what you want and attach references.
					</p>
				</article>
				<article className="rounded-lg border border-zinc-200 p-4 dark:border-zinc-700">
					<p className="text-sm font-semibold text-zinc-500">Step 2</p>
					<h3 className="mt-1 font-semibold">Confirm Quote</h3>
					<p className="mt-2 text-sm text-zinc-500">
						Review timeline, category, and placeholder price estimate.
					</p>
				</article>
				<article className="rounded-lg border border-zinc-200 p-4 dark:border-zinc-700">
					<p className="text-sm font-semibold text-zinc-500">Step 3</p>
					<h3 className="mt-1 font-semibold">Receive Final Art</h3>
					<p className="mt-2 text-sm text-zinc-500">
						Get high-resolution files and optional commercial license notes.
					</p>
				</article>
			</section>

			{/* Client Feedback & FAQ */}
			<section className="grid gap-4 rounded-xl border border-zinc-200 p-6 dark:border-zinc-800 lg:grid-cols-2">
				<article>
					<h2 className="text-2xl font-semibold">Client Feedback</h2>
					<div className="mt-4 space-y-3">
						<blockquote className="rounded-md border border-dashed border-zinc-300 p-3 text-sm text-zinc-500 dark:border-zinc-700">
							"[Placeholder testimonial] Vex nailed the style and delivered on
							time."
						</blockquote>
						<blockquote className="rounded-md border border-dashed border-zinc-300 p-3 text-sm text-zinc-500 dark:border-zinc-700">
							"[Placeholder testimonial] Great communication and smooth revision
							process."
						</blockquote>
					</div>
				</article>
				<article>
					<h2 className="text-2xl font-semibold">FAQ</h2>
					<div className="mt-4 space-y-3">
						{faqs.map((item) => (
							<details
								key={item.q}
								className="rounded-md border border-zinc-200 p-3 dark:border-zinc-700"
							>
								<summary className="cursor-pointer font-medium">{item.q}</summary>
								<p className="mt-2 text-sm text-zinc-500">{item.a}</p>
							</details>
						))}
					</div>
				</article>
			</section>

			{/* Contact Section */}
			<section
				id="contact"
				className="rounded-xl border border-zinc-200 bg-zinc-50 p-6 text-center dark:border-zinc-800 dark:bg-zinc-900/60"
			>
				<h2 className="text-2xl font-semibold">Ready to Commission me twin?</h2>
				<p className="mx-auto mt-2 max-w-2xl text-zinc-600 dark:text-zinc-300">
					Fill out the form below to request a commission.
				</p>

				<form onSubmit={handleSubmit} className="mt-6 flex flex-col gap-4 max-w-md mx-auto text-left">
					<div>
						<label className="block text-sm font-medium mb-1">Name</label>
						<input required type="text" value={senderName} onChange={e => setSenderName(e.target.value)} className="w-full border border-zinc-300 rounded px-3 py-2 bg-white dark:bg-zinc-800 dark:border-zinc-700" placeholder="Your Name" />
					</div>
					<div>
						<label className="block text-sm font-medium mb-1">Email</label>
						<input required type="email" value={senderEmail} onChange={e => setSenderEmail(e.target.value)} className="w-full border border-zinc-300 rounded px-3 py-2 bg-white dark:bg-zinc-800 dark:border-zinc-700" placeholder="Your Email" />
					</div>
					<div>
						<label className="block text-sm font-medium mb-1">Commission Type</label>
						<select value={commissionType} onChange={e => setCommissionType(e.target.value)} className="w-full border border-zinc-300 rounded px-3 py-2 bg-white dark:bg-zinc-800 dark:border-zinc-700">
							<option value="HEADSHOT">Headshot</option>
							<option value="HALFBODY">Half-Body</option>
							<option value="FULLBODY">Full-Body</option>
						</select>
					</div>
                    <div>
						<label className="block text-sm font-medium mb-1">Artstyle Type</label>
						<select value={artstyleType} onChange={e => setArtstyleType(e.target.value)} className="w-full border border-zinc-300 rounded px-3 py-2 bg-white dark:bg-zinc-800 dark:border-zinc-700">
							<option value="SKETCH">Sketch</option>
							<option value="SIMPLE">Simple</option>
							<option value="RENDER">Render</option>
							<option value="PAINTING">Painting</option>
						</select>
					</div>

					<div>
						<label className="block text-sm font-medium mb-1">Additional Notes</label>
						<textarea required value={additionalNotes} onChange={e => setAdditionalNotes(e.target.value)} className="w-full border border-zinc-300 rounded px-3 py-2 bg-white dark:bg-zinc-800 dark:border-zinc-700" rows={4} placeholder="Describe your character, references, ideas, etc..." />
					</div>
					<button type="submit" className="mt-2 text-white font-bold py-2 px-4 rounded" style={{ backgroundColor: "black" }}>
						Submit Commission Request
					</button>
					{status && <p className="text-center mt-2 font-medium text-zinc-800 dark:text-zinc-200">{status}</p>}
				</form>
			</section>
		</main>
	);
}

