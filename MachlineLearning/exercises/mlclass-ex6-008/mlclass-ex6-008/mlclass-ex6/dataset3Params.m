function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%

range = [0.01 0.03 0.1 0.3 1 3 10 30];
err = 1;
opt_c = range(1);
opt_sigma = range(1);

for i = 1:length(range)
	c = range(i);
	for j = 1:length(range)
		sigma = range(j);
		% train model
		model = svmTrain(X, y, c, @(x1, x2) gaussianKernel(x1, x2, sigma));
		% predict
		predictions = svmPredict(model, Xval);
		suberr = mean(double(predictions ~= yval));
		if suberr < err
			err = suberr;
			opt_c = c;
			opt_sigma = sigma;
		end
	end
end

C = opt_c;
sigma = opt_sigma;





% =========================================================================

end
