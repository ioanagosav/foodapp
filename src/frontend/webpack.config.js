
module.exports = {
	entry: "./src/app.js",
	output: {
		filename: './dist/app.js'
	},

	module: {
		loaders: [
			{
				test: /\.jsx?/,
				loader: 'babel'
			}
		]
	}
};
